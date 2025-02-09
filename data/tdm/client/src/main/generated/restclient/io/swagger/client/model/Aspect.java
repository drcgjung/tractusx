/*
 * Catena-X Speedboat Test Data Generator
 * Disclaimer: This service serves synthetic, none-productive data for testing purposes only. All BOMs, part trees, VINs, serialNos etc. are synthetic
 *
 * OpenAPI spec version: 0.0.1 Speedboat
 * Contact: christian.kabelin@partner.bmw.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Aspect location data
 */@Schema(description = "Aspect location data")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaClientCodegen", date = "2021-10-03T20:34:34.146648200+02:00[Europe/Berlin]")
public class Aspect {

  
  @JsonProperty("name")
  private String name = null;
  
  
  @JsonProperty("url")
  private String url = null;
  
  public Aspect name(String name) {
    this.name = name;
    return this;
  }

  
  /**
  * Aspect name
  * @return name
  **/
  
  
  @Schema(example = "CE", description = "Aspect name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  public Aspect url(String url) {
    this.url = url;
    return this;
  }

  
  /**
  * URL location of aspect data
  * @return url
  **/
  
  
  @Schema(example = "http://aspects-url/CE", description = "URL location of aspect data")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Aspect aspect = (Aspect) o;
    return Objects.equals(this.name, aspect.name) &&
        Objects.equals(this.url, aspect.url);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(name, url);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Aspect {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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



