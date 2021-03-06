/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.sub.runtime.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionDto;
import org.camunda.bpm.engine.rest.dto.runtime.CaseExecutionTriggerDto;
import org.camunda.bpm.engine.rest.dto.runtime.TriggerVariableValueDto;
import org.camunda.bpm.engine.rest.dto.runtime.VariableNameDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.sub.VariableResource;
import org.camunda.bpm.engine.rest.sub.runtime.CaseExecutionResource;
import org.camunda.bpm.engine.rest.util.DtoUtil;
import org.camunda.bpm.engine.runtime.CaseExecution;
import org.camunda.bpm.engine.runtime.CaseExecutionCommandBuilder;

/**
 *
 * @author Roman Smirnov
 *
 */
public class CaseExecutionResourceImpl implements CaseExecutionResource {

  protected ProcessEngine engine;
  protected String caseExecutionId;

  public CaseExecutionResourceImpl(ProcessEngine engine, String caseExecutionId) {
    this.engine = engine;
    this.caseExecutionId = caseExecutionId;
  }

  public CaseExecutionDto getCaseExecution() {
    CaseService caseService = engine.getCaseService();

    CaseExecution execution = caseService
        .createCaseExecutionQuery()
        .caseExecutionId(caseExecutionId)
        .singleResult();

    if (execution == null) {
      throw new InvalidRequestException(Status.NOT_FOUND, "Case execution with id " + caseExecutionId + " does not exist.");
    }

    CaseExecutionDto result = CaseExecutionDto.fromCaseExecution(execution);
    return result;
  }

  public void manualStart(CaseExecutionTriggerDto triggerDto) {
    try {
      CaseService caseService = engine.getCaseService();
      CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

      initializeCommand(commandBuilder, triggerDto, "start manually");

      commandBuilder.manualStart();

    } catch (ProcessEngineException e) {
      throw new InvalidRequestException(Status.BAD_REQUEST, e, "Cannot start case execution with id '" + caseExecutionId + "' manually.");
    }

  }

  public void disable(CaseExecutionTriggerDto triggerDto) {
    try {
      CaseService caseService = engine.getCaseService();
      CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

      initializeCommand(commandBuilder, triggerDto, "disable");

      commandBuilder.disable();

    } catch (ProcessEngineException e) {
      throw new InvalidRequestException(Status.BAD_REQUEST, e, "Cannot disable case execution with id '" + caseExecutionId + "'.");
    }
  }

  public void reenable(CaseExecutionTriggerDto triggerDto) {
    try {
      CaseService caseService = engine.getCaseService();
      CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

      initializeCommand(commandBuilder, triggerDto, "reenable");

      commandBuilder.reenable();
    } catch (ProcessEngineException e) {
      throw new InvalidRequestException(Status.BAD_REQUEST, e, "Cannot re-enable case execution with id '" + caseExecutionId + "'.");
    }
  }

  public void complete(CaseExecutionTriggerDto triggerDto) {
    try {
      CaseService caseService = engine.getCaseService();
      CaseExecutionCommandBuilder commandBuilder = caseService.withCaseExecution(caseExecutionId);

      initializeCommand(commandBuilder, triggerDto, "complete");

      commandBuilder.complete();
    } catch (ProcessEngineException e) {
      throw new InvalidRequestException(Status.BAD_REQUEST, e, "Cannot complete case execution with id '" + caseExecutionId + "'.");
    }
  }

  protected void initializeCommand(CaseExecutionCommandBuilder commandBuilder, CaseExecutionTriggerDto triggerDto, String transition) {
    Map<String, TriggerVariableValueDto> variables = triggerDto.getVariables();
    if (variables != null && !variables.isEmpty()) {
      initializeCommandWithVariables(commandBuilder, variables, transition);
    }

    List<VariableNameDto> deletions = triggerDto.getDeletions();
    if (deletions != null && !deletions.isEmpty()) {
      initializeCommandWithDeletions(commandBuilder, deletions, transition);
    }
  }

  protected void initializeCommandWithVariables(CaseExecutionCommandBuilder commandBuilder, Map<String, TriggerVariableValueDto> variables, String transition) {
    for(String variableName : variables.keySet()) {
      try {
        TriggerVariableValueDto variableValue = variables.get(variableName);
        Object value = DtoUtil.toType(variableValue.getType(), variableValue.getValue());

        if (variableValue.isLocal()) {
          commandBuilder.setVariableLocal(variableName, value);

        } else {
          commandBuilder.setVariable(variableName, value);
        }

      } catch (NumberFormatException e) {
        String errorMessage = String.format("Cannot %s case execution %s due to number format exception of variable %s: %s", transition, caseExecutionId, variableName, e.getMessage());
        throw new RestException(Status.BAD_REQUEST, e, errorMessage);

      } catch (ParseException e) {
        String errorMessage = String.format("Cannot %s case execution %s due to parse exception of variable %s: %s", transition, variableName, variableName, e.getMessage());
        throw new RestException(Status.BAD_REQUEST, e, errorMessage);

      } catch (IllegalArgumentException e) {
        String errorMessage = String.format("Cannot %s case execution %s because of variable %s: %s", transition, variableName, variableName, e.getMessage());
        throw new RestException(Status.BAD_REQUEST, errorMessage);
      }
    }
  }

  protected void initializeCommandWithDeletions(CaseExecutionCommandBuilder commandBuilder, List<VariableNameDto> deletions, String transition) {
    for (VariableNameDto variableName : deletions) {
      if (variableName.isLocal()) {
        commandBuilder.removeVariableLocal(variableName.getName());
      } else {
        commandBuilder.removeVariable(variableName.getName());
      }
    }
  }

  public VariableResource getVariablesLocal() {
    return new LocalCaseExecutionVariablesResource(engine, caseExecutionId);
  }

  public VariableResource getVariables() {
    return new CaseExecutionVariablesResource(engine, caseExecutionId);
  }

}
