import UserService from "../../helpers/UserService";
import { SparqlResult, Skill } from './interfaces';

const SPARQL_URL = `http://localhost:8181/api/sparql/agent`;

const camelize = (str:String) => {
    return str.split(" ").map( function(text:String) {
      return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
    }).join(" ");
}

/**
 * Example of a skill which computes
 * aggregated measures over groups
 * of vehicles
 */

const materialQuestionExp : RegExp = /^.*much ([A-Za-z ]+) is .* (vehicle model [A-Za-z]).*$/i;

const materialSkill : Skill = {
  nick: "material",
  name: "Material Skill (T-Systems)",

  match: function(utterance:string) {
     if(materialQuestionExp.test(utterance)) {
        let match=utterance.match(materialQuestionExp)
        let material=camelize(match[1]);
        let vehicleType=camelize(match[2]);
        let query=`
PREFIX cx: <https://github.com/eclipse/tractusx/>
PREFIX qudt: <http://qudt.org/schema/qudt/>

SELECT ?vehicleType ?vehicle ?material (SUM(?component_weight) as ?total_weight)
WHERE {

    BIND("${vehicleType}" as ?vehicleType).
    BIND("${material}" as ?material).

    # We're looking for the fitting vehicles, which are Serialized Parts of Tenant2
    SERVICE <http://localhost:8183/api/sparql/agent> {
        GRAPH <urn:cx:graph#serializedPart> {
            ?vehicle a cx:SerializedPart.
            ?vehicle cx:belongsTo ?specific_vehicle_type.
            ?specific_vehicle_type cx:hasPartTypeName ?vehicleType .
        }
    }

    # We need the BOM explosion of the vehicles which are assembled by Tenant1
    SERVICE <http://localhost:8182/api/sparql/agent> {
        GRAPH <urn:cx:graph#assemblyPartRelation> {
            ?vehicle cx:consistsOf* ?vehicle_component .
        }
    }

    # Access the specific Material in Tenant2
    SERVICE <http://localhost:8183/api/sparql/agent> {
        GRAPH <urn:cx:graph#serializedPart> {
            ?vehicle_component cx:belongsTo ?vehicle_component_type.
            ?vehicle_component_type cx:hasPartTypeName ?material .
        }
    }

    # Get the last weight measure from the assembly in Tenant1
    SERVICE <http://localhost:8182/api/sparql/agent> {
        GRAPH <urn:cx:graph#assemblyPartRelation> {
            ?vehicle_component cx:isAssembledThrough ?assemble_event .
            ?assemble_event qudt:NumericValue ?component_weight .
        }
    }

}  GROUP BY ?vehicle ?vehicleType ?material`;
        return query;
     } else {
        return null;
     }
  },

  summarize: function(res:SparqlResult) {
    var material = res.results.bindings[0]["material"]["value"];
    var model = res.results.bindings[0]["vehicleType"]["value"];
    var weight=0.0;
    res.results.bindings.forEach( binding => { weight=weight+Number(binding["total_weight"]["value"].replace("e0","")); });
    weight=weight/res.results.bindings.length;

    return {
      phrases:[`Hey Schorsch`, `There are`, `on average`,`${weight} kg`, `of`, `${material}`, `in`,`${model}`],
      pauses:[0,0,0,0,0,0,0,0]
    };
  }
};

/**
 * Example of a skill which lists vehicles
 * containing a certain component
 */
const traceQuestionExp : RegExp = /^.*which contain ([A-Za-z ]+).*$/i;

const traceSkill : Skill = {
  nick: "trace",
  name: "Tracing Skill (BMW)",

  match: function(utterance:string) {
     if(traceQuestionExp.test(utterance)) {
        let match=utterance.match(traceQuestionExp)
        let material=camelize(match[1]);
        let query=`
PREFIX cx: <https://github.com/eclipse/tractusx/>
PREFIX qudt: <http://qudt.org/schema/qudt/>
SELECT DISTINCT ?vehicle ?material
WHERE {
    BIND("${material}" as ?material).

   # From the SerializedPart of tenant2 we filter a specific Material
    SERVICE <http://localhost:8183/api/sparql/agent> {
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
    SERVICE <http://localhost:8182/api/sparql/agent> {                      # Connector Data Plane of Tenant1
        GRAPH <urn:cx:graph#assemblyPartRelation> {                         # Graph asset of Tenant2
            ?vehicle cx:consistsOf* ?material_component .
        }
    }
}`;
        return query;
     } else {
        return null;
     }
  },

  summarize: function(res:SparqlResult) {
    var material = res.results.bindings[0]["material"]["value"];
    return {
         phrases:[`Hey Schorsch`, `${material}`, `is contained in`,res.results.bindings.length.toString(),"vehicles"],
         pauses:[0,10,5,5,0]
    };
  }
};

export const unknownSkill : Skill = {
  nick: "unknown",

  name: "Unknown Skill",

  match: function(utterance:string) {
     return "";
  },

  summarize: function(res:SparqlResult) {
    return null;
  }
};

export const skills:Skill[] = [
 materialSkill,
 traceSkill,
 unknownSkill
];

function handleRequest(res: Response) {
  if(res.status >= 400) {
    throw new Error(`${res.statusText}: Server responds with ${res.status} error!`);
  }
  let result=res.json();
  return result;
}

export function getQuery(query:string){
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
  return fetch(`${SPARQL_URL}`, requestOptions).then(handleRequest);
}

