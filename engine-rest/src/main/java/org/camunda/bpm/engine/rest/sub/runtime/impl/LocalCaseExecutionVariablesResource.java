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

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.sub.impl.AbstractVariablesResource;

/**
 *
 * @author Roman Smirnov
 *
 */
public class LocalCaseExecutionVariablesResource extends AbstractVariablesResource {

  public LocalCaseExecutionVariablesResource(ProcessEngine engine, String resourceId) {
    super(engine, resourceId);
  }

  protected Map<String, Object> getVariableEntities() {
    CaseService caseService = engine.getCaseService();
    return caseService.getVariablesLocal(resourceId);
  }

  protected void updateVariableEntities(Map<String, Object> variables, List<String> deletions) {
    CaseService caseService = engine.getCaseService();
    caseService
      .withCaseExecution(resourceId)
      .setVariablesLocal(variables)
      .removeVariablesLocal(deletions)
      .execute();
  }

  protected Object getVariableEntity(String variableKey) {
    CaseService caseService = engine.getCaseService();
    return caseService.getVariableLocal(resourceId, variableKey);
  }

  protected void setVariableEntity(String variableKey, Object variableValue) {
    CaseService caseService = engine.getCaseService();
    caseService
      .withCaseExecution(resourceId)
      .setVariableLocal(variableKey, variableValue)
      .execute();
  }

  protected void removeVariableEntity(String variableKey) {
    CaseService caseService = engine.getCaseService();
    caseService
      .withCaseExecution(resourceId)
      .removeVariableLocal(variableKey)
      .execute();
  }

  protected String getResourceTypeName() {
    return "case execution";
  }

}
