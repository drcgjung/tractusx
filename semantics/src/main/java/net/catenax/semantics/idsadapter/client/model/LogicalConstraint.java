/*
 * Dataspace Connector
 * IDS Connector originally developed by the Fraunhofer ISST
 *
 * OpenAPI spec version: 6.2.0
 * Contact: info@dataspace-connector.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package net.catenax.semantics.idsadapter.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
import net.catenax.semantics.idsadapter.client.model.AbstractConstraint;
import net.catenax.semantics.idsadapter.client.model.Constraint;
import net.catenax.semantics.idsadapter.client.model.TypedLiteral;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * LogicalConstraint
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-09-08T16:15:16.333286600+02:00[Europe/Berlin]")@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_atType", visible = true )
@JsonSubTypes({
})

public class LogicalConstraint extends AbstractConstraint implements OneOfRuleIdsconstraintItems, OneOfRuleIdsassetRefinement {
  @JsonProperty("ids:and")
  private List<Constraint> idsand = null;

  @JsonProperty("ids:or")
  private List<Constraint> idsor = null;

  @JsonProperty("ids:xone")
  private List<Constraint> idsxone = null;

  @JsonProperty("@type")
  private String _atType = null;

  public LogicalConstraint idsand(List<Constraint> idsand) {
    this.idsand = idsand;
    return this;
  }

  public LogicalConstraint addIdsandItem(Constraint idsandItem) {
    if (this.idsand == null) {
      this.idsand = new ArrayList<>();
    }
    this.idsand.add(idsandItem);
    return this;
  }

   /**
   * Get idsand
   * @return idsand
  **/
  @Schema(description = "")
  public List<Constraint> getIdsand() {
    return idsand;
  }

  public void setIdsand(List<Constraint> idsand) {
    this.idsand = idsand;
  }

  public LogicalConstraint idsor(List<Constraint> idsor) {
    this.idsor = idsor;
    return this;
  }

  public LogicalConstraint addIdsorItem(Constraint idsorItem) {
    if (this.idsor == null) {
      this.idsor = new ArrayList<>();
    }
    this.idsor.add(idsorItem);
    return this;
  }

   /**
   * Get idsor
   * @return idsor
  **/
  @Schema(description = "")
  public List<Constraint> getIdsor() {
    return idsor;
  }

  public void setIdsor(List<Constraint> idsor) {
    this.idsor = idsor;
  }

  public LogicalConstraint idsxone(List<Constraint> idsxone) {
    this.idsxone = idsxone;
    return this;
  }

  public LogicalConstraint addIdsxoneItem(Constraint idsxoneItem) {
    if (this.idsxone == null) {
      this.idsxone = new ArrayList<>();
    }
    this.idsxone.add(idsxoneItem);
    return this;
  }

   /**
   * Get idsxone
   * @return idsxone
  **/
  @Schema(description = "")
  public List<Constraint> getIdsxone() {
    return idsxone;
  }

  public void setIdsxone(List<Constraint> idsxone) {
    this.idsxone = idsxone;
  }

  public LogicalConstraint _atType(String _atType) {
    this._atType = _atType;
    return this;
  }

   /**
   * Get _atType
   * @return _atType
  **/
  @Schema(required = true, description = "")
  public String getAtType() {
    return _atType;
  }

  public void setAtType(String _atType) {
    this._atType = _atType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogicalConstraint logicalConstraint = (LogicalConstraint) o;
    return Objects.equals(this.idsand, logicalConstraint.idsand) &&
        Objects.equals(this.idsor, logicalConstraint.idsor) &&
        Objects.equals(this.idsxone, logicalConstraint.idsxone) &&
        Objects.equals(this._atType, logicalConstraint._atType) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idsand, idsor, idsxone, _atType, super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogicalConstraint {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    idsand: ").append(toIndentedString(idsand)).append("\n");
    sb.append("    idsor: ").append(toIndentedString(idsor)).append("\n");
    sb.append("    idsxone: ").append(toIndentedString(idsxone)).append("\n");
    sb.append("    _atType: ").append(toIndentedString(_atType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
