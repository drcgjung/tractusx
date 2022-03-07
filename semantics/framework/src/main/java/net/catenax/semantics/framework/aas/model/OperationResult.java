package net.catenax.semantics.framework.aas.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * OperationResult
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-03-04T18:11:14.812382100+01:00[Europe/Berlin]")


public class OperationResult   {
  @JsonProperty("executionResult")
  private Result executionResult = null;

  /**
   * Gets or Sets executionState
   */
  public enum ExecutionStateEnum {
    INITIATED("Initiated"),
    
    RUNNING("Running"),
    
    COMPLETED("Completed"),
    
    CANCELED("Canceled"),
    
    FAILED("Failed"),
    
    TIMEOUT("Timeout");

    private String value;

    ExecutionStateEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ExecutionStateEnum fromValue(String text) {
      for (ExecutionStateEnum b : ExecutionStateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("executionState")
  private ExecutionStateEnum executionState = null;

  @JsonProperty("inoutputArguments")
  @Valid
  private List<OperationVariable> inoutputArguments = null;

  @JsonProperty("outputArguments")
  @Valid
  private List<OperationVariable> outputArguments = null;

  @JsonProperty("requestId")
  private String requestId = null;

  public OperationResult executionResult(Result executionResult) {
    this.executionResult = executionResult;
    return this;
  }

  /**
   * Get executionResult
   * @return executionResult
   **/
  @Schema(description = "")
  
    @Valid
    public Result getExecutionResult() {
    return executionResult;
  }

  public void setExecutionResult(Result executionResult) {
    this.executionResult = executionResult;
  }

  public OperationResult executionState(ExecutionStateEnum executionState) {
    this.executionState = executionState;
    return this;
  }

  /**
   * Get executionState
   * @return executionState
   **/
  @Schema(description = "")
  
    public ExecutionStateEnum getExecutionState() {
    return executionState;
  }

  public void setExecutionState(ExecutionStateEnum executionState) {
    this.executionState = executionState;
  }

  public OperationResult inoutputArguments(List<OperationVariable> inoutputArguments) {
    this.inoutputArguments = inoutputArguments;
    return this;
  }

  public OperationResult addInoutputArgumentsItem(OperationVariable inoutputArgumentsItem) {
    if (this.inoutputArguments == null) {
      this.inoutputArguments = new ArrayList<>();
    }
    this.inoutputArguments.add(inoutputArgumentsItem);
    return this;
  }

  /**
   * Get inoutputArguments
   * @return inoutputArguments
   **/
  @Schema(description = "")
      @Valid
    public List<OperationVariable> getInoutputArguments() {
    return inoutputArguments;
  }

  public void setInoutputArguments(List<OperationVariable> inoutputArguments) {
    this.inoutputArguments = inoutputArguments;
  }

  public OperationResult outputArguments(List<OperationVariable> outputArguments) {
    this.outputArguments = outputArguments;
    return this;
  }

  public OperationResult addOutputArgumentsItem(OperationVariable outputArgumentsItem) {
    if (this.outputArguments == null) {
      this.outputArguments = new ArrayList<>();
    }
    this.outputArguments.add(outputArgumentsItem);
    return this;
  }

  /**
   * Get outputArguments
   * @return outputArguments
   **/
  @Schema(description = "")
      @Valid
    public List<OperationVariable> getOutputArguments() {
    return outputArguments;
  }

  public void setOutputArguments(List<OperationVariable> outputArguments) {
    this.outputArguments = outputArguments;
  }

  public OperationResult requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Get requestId
   * @return requestId
   **/
  @Schema(description = "")
  
    public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperationResult operationResult = (OperationResult) o;
    return Objects.equals(this.executionResult, operationResult.executionResult) &&
        Objects.equals(this.executionState, operationResult.executionState) &&
        Objects.equals(this.inoutputArguments, operationResult.inoutputArguments) &&
        Objects.equals(this.outputArguments, operationResult.outputArguments) &&
        Objects.equals(this.requestId, operationResult.requestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executionResult, executionState, inoutputArguments, outputArguments, requestId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperationResult {\n");
    
    sb.append("    executionResult: ").append(toIndentedString(executionResult)).append("\n");
    sb.append("    executionState: ").append(toIndentedString(executionState)).append("\n");
    sb.append("    inoutputArguments: ").append(toIndentedString(inoutputArguments)).append("\n");
    sb.append("    outputArguments: ").append(toIndentedString(outputArguments)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
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
