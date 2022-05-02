import UserService from "../../helpers/UserService";

// limitations under the License.
const SPARQL_URL = `http://localhost:8181/api/sparql/hub`;

function handleRequest(res: Response){
  if(res.status >= 400) {
    throw new Error(`${res.statusText}: Server responds with ${res.status} error!`);
  }
  let result=res.json();
  return result;
}

export function getQuery(params = { "query":"key", "vehicleType" : "Vehicle Model A", "material":"Glue" }){
  console.log(params);

  var query:string = "";
  if(params.query=="material") {
    query=`PREFIX cx: <https://github.com/eclipse/tractusx/>
                     PREFIX qudt: <http://qudt.org/schema/qudt/>
                     SELECT ?vehicleType ?vehicle ?material (SUM(?component_weight) as ?total_weight)
                     WHERE {

                         BIND("${params.vehicleType}" as ?vehicleType).
                         BIND("${params.material}" as ?material).

                         # We're looking for a vehicle, which is a Serialized Part of Tenant2
                         SERVICE <http://localhost:8183/api/sparql/hub> {
                             GRAPH <urn:cx:graph#serializedPart> {
                                 ?vehicle a cx:SerializedPart.
                                 ?vehicle cx:belongsTo ?specific_vehicle_type.
                                 ?specific_vehicle_type cx:hasPartTypeName ?vehicleType .
                             }
                         }

                         # We need the BOM of the vehicles which are provided by Tenant1
                         SERVICE <http://localhost:8182/api/sparql/hub> {
                             GRAPH <urn:cx:graph#assemblyPartRelation> {
                                 ?vehicle cx:consistsOf* ?vehicle_component .
                             }
                         }


                         # From the BOM we filter a specific Material
                         SERVICE <http://localhost:8183/api/sparql/hub> {
                             GRAPH <urn:cx:graph#serializedPart> {
                                 ?vehicle_component cx:belongsTo ?vehicle_component_type.
                                 ?vehicle_component_type cx:hasPartTypeName ?material .
                             }
                         }

                         # Now we need to check, how it is assembled
                         SERVICE <http://localhost:8182/api/sparql/hub> {
                             GRAPH <urn:cx:graph#assemblyPartRelation> {
                                 ?vehicle_component cx:isAssembledThrough ?assemble_event .
                                 ?assemble_event qudt:NumericValue ?component_weight .
                             }
                         }

                     }  GROUP BY ?vehicle ?vehicleType ?material`;
  } else if(params.query=="trace") {
    query=`PREFIX cx: <https://github.com/eclipse/tractusx/>
           PREFIX qudt: <http://qudt.org/schema/qudt/>
           SELECT DISTINCT ?vehicle ?material
           WHERE {
                  BIND("${params.material}" as ?material).

                  # From the SerializedPart of tenant2 we filter a specific Material
                  SERVICE <http://localhost:8183/api/sparql/hub> {
                    GRAPH <urn:cx:graph#serializedPart> {
                        ?material_type cx:hasPartTypeName ?material .
                        ?material_component a cx:SerializedPart ;
                                 cx:belongsTo ?material_type .
                        ?vehicle a cx:SerializedPart.
                        ?vehicle cx:belongsTo ?specific_vehicle_type.
                        ?specific_vehicle_type cx:hasPartTypeName "Vehicle Model A" .
                    }
                  }


                  # We need the BOM of the vehicles which are provided by Tenant1
                  SERVICE <http://localhost:8182/api/sparql/hub> {                        # Connector Data Plane of Tenant1
                    GRAPH <urn:cx:graph#assemblyPartRelation> {                         # Graph asset of Tenant2
                       ?vehicle cx:consistsOf* ?matematerial_component .
                    }
                  }
           }`;
  } else {
    return;
  }

  const requestOptions = {
    method: 'POST',
    headers: new Headers({
        "Content-Type": "application/sparql-query",
        "Accept":"application/json",
        "catenax-connector-context":"urn:connector:app:semantics:catenax:net",
        "catenax-security-token":"mock-eu",
        "catenax-correlation-id":"4242",
    }),
    body:query
  }
  return fetch(`${SPARQL_URL}`, requestOptions)
    .then(handleRequest);
}
