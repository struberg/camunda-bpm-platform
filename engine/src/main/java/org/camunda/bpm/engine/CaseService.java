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
package org.camunda.bpm.engine;

import org.camunda.bpm.engine.runtime.CaseExecution;
import org.camunda.bpm.engine.runtime.CaseExecutionCommandBuilder;
import org.camunda.bpm.engine.runtime.CaseExecutionQuery;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.runtime.CaseInstanceBuilder;
import org.camunda.bpm.engine.runtime.CaseInstanceQuery;

/**
 * Service which provides access to {@link CaseInstance case instances}
 * and {@link CaseExecution case executions}.
 *
 * @author Roman Smirnov
 *
 */
public interface CaseService {

  /**
   * <p>Define a {@link CaseInstance} using a fluent builder.</p>
   *
   * @param caseDefinitionKey The key of case definition to create a new case instance in
   * the latest version of the case definition with the given key, cannot be null.
   *
   * @return a {@link CaseInstanceBuilder fluent builder} for defining a new case instance
   */
  CaseInstanceBuilder createCaseInstanceByKey(String caseDefinitionKey);

  /**
   * <p>Define a {@link CaseInstance} using a fluent builder.</p>
   *
   * <p>Starts a new case instance in the exactly specified version of the case definition
   * with the given id.</p>
   *
   * @param caseDefinitionId The id of case definition to create a new case instance in
   * the exactly specified version of the case definition with the given id, cannot be null.
   *
   * @return a {@link CaseInstanceBuilder fluent builder} for defining a new case instance
   */
  CaseInstanceBuilder createCaseInstanceById(String caseDefinitionId);

  /**
   * <p>Creates a new {@link CaseInstanceQuery} instance, that can be used
   * to query case instances.</p>
   */
  CaseInstanceQuery createCaseInstanceQuery();

  /**
   * <p>Creates a new {@link CaseExecutionQuery} instance,
   * that can be used to query the executions and case instances.</p>
   */
  CaseExecutionQuery createCaseExecutionQuery();

  /**
   * <p>Define a command to be executed for a {@link CaseExecution} using a fluent builder.</p>
   *
   * @param caseExecutionId The id of a case execution to define a command for it.
   *
   * @return a {@link CaseExecutionCommandBuilder fluent builder} for defining a command
   *         for a case execution
   */
  CaseExecutionCommandBuilder withCaseExecution(String caseExecutionId);
}
