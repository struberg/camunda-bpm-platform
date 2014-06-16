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
package org.camunda.bpm.engine.runtime;

import java.util.Collection;
import java.util.Map;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.model.cmmn.instance.Stage;
import org.camunda.bpm.model.cmmn.instance.Task;

/**
 * <p>A fluent builder for defining a command to execute for a case execution.</p>
 *
 * <p>This fluent builder offers different points to execute a defined command:
 *  <ul>
 *    <li>{@link #execute()}</li>
 *    <li>{@link #manualStart()}</li>
 *    <li>{@link #disable()}</li>
 *    <li>{@link #enable()}</li>
 *  </ul>
 * </p>
 *
 * <p>The entry point to use this fluent builder is {@link CaseService#withCaseExecution(String)}.
 * It expects an id of a case execution as parameter.</p>
 *
 * <p>This fluent builder can be used as follows:</p>
 *
 * <p>(1) Set and remove case execution variables:</p>
 * <code>
 * &nbsp;&nbsp;caseService<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.withCaseExecution("aCaseDefinitionId")<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.setVariable("aVariableName", "aVariableValue")<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.setVariable("anotherVariableName", 999)<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.removeVariable("aVariableNameToRemove")<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.execute();
 * </code>
 *
 * <p>(2) Set case execution variable and start the case execution manually:</p>
 * <code>
 * &nbsp;&nbsp;caseService<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.withCaseExecution("aCaseDefinitionId")<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.setVariable("aVariableName", "aVariableValue")<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;.manualStart();
 * </code>
 *
 * <p>etc.</p>
 *
 * <p><strong>Note:</strong> All defined changes for a case execution within this fluent
 * builder will be performed in one command. So for example: if you set and remove
 * variables of a case execution this happens in a single command. This has the effect
 * that if anything went wrong the whole command will be rolled back.</p>
 *
 * @author Roman Smirnov
 *
 */
public interface CaseExecutionCommandBuilder {

  /**
   * <p>Pass a variable to the case execution. If the variable is not already
   * existing, they will be created in the case instance (which is the root execution)
   * otherwise the existing variable will be updated.</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variables.</p>
   *
   * @param variableName the name of the variable to set
   * @param variableValue the value of the variable to set
   * @return the builder
   */
  CaseExecutionCommandBuilder setVariable(String variableName, Object variableValue);

  /**
   * <p>Pass a map of variables to the case execution. If the variables are not already
   * existing, they will be created in the case instance (which is the root execution)
   * otherwise the existing variable will be updated.</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variables.</p>
   *
   * @param variables the map of variables
   * @return the builder
   */
  CaseExecutionCommandBuilder setVariables(Map<String, Object> variables);

  /**
   * <p>Pass a local variable to the case execution (not considering parent scopes).</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variables.</p>
   *
   * @param variableName the name of the variable to set
   * @param variableValue the value of the variable to set
   * @return the builder
   */
  CaseExecutionCommandBuilder setVariableLocal(String variableName, Object variableValue);

  /**
   * <p>Pass a map of variables to the case execution (not considering parent scopes).</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variables.</p>
   *
   * @param variables the map of variables
   * @return the builder
   */
  CaseExecutionCommandBuilder setVariablesLocal(Map<String, Object> variables);

  /**
   * <p>Pass a variable name of a variable to be removed for a case execution.</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variable names.</p>
   *
   * @param variableName the name of a variable to remove
   * @return the builder
   */
  CaseExecutionCommandBuilder removeVariable(String variableName);

  /**
   * <p>Pass a collection of variable names of variables to be removed for a
   * case execution.</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variable names.</p>
   *
   * @param variableNames a collection of names of variables to remove
   * @return the builder
   */
  CaseExecutionCommandBuilder removeVariables(Collection<String> variableNames);

  /**
   * <p>Pass a variable name of a local variable to be removed for a case execution
   * (not considering parent scopes).</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variable names.</p>
   *
   * @param variableName the name of a variable to remove
   * @return the builder
   */
  CaseExecutionCommandBuilder removeVariableLocal(String variableName);

  /**
   * <p>Pass a collection of variable names of local variables to be removed for a
   * case execution (not considering parent scopes).</p>
   *
   * <p>Invoking this method multiple times allows passing multiple variable names.</p>
   *
   * @param variableNames a collection of names of variables to remove
   * @return the builder
   */
  CaseExecutionCommandBuilder removeVariablesLocal(Collection<String> variableNames);

  /**
   * <p>Invoking this method will remove and/or set the passed variables.</p>
   *
   * <p>This behaves as follows:</p>
   *
   * <ol>
   *   <li>if at least one variable name of a variable to remove is passed, those
   *       variables will be removed.
   *   </li>
   *   <li>if at least one local variable name of a local variable to remove is
   *       passed, those local variables will be removed.
   *   </li>
   *   <li>if at least one variable to add or update is passed, those variables
   *       will be set for a case execution.
   *   </li>
   *   <li>if at least one local variable to add or update is passed, those
   *       variables will be set for a case execution.
   *   </li>
   * </ol>
   *
   *
   * @throws ProcessEngineException this exception will be thrown
   *  <ul>
   *    <li>when the passed case execution id is null or</li>
   *    <li>when no case execution is found for the given case execution id</li>
   *  </ul>
   */
  void execute();

  /**
   * <p>Additionally to {@link #execute()} the associated case execution will
   * be started manually. Therefore the happens a transition from the state
   * <code>ENABLED</code> to state <code>ACTIVE</code>.</p>
   *
   * <p>According to CMMN 1.0 specification the state <code>ACTIVE</code> means,
   * that the with the case execution related {@link Stage} or {@link Task} is
   * executing in this state.</p>
   *
   * <p>In case of {@link Task} a new {@link org.camunda.bpm.engine.task.Task user task}
   * can be executed. And in case of a {@link Stage} the contained planned items will
   * be created.</p>
   *
   * @throws ProcessEngineException this exception will be thrown
   *  <ul>
   *    <li>when the passed case execution id is null or</li>
   *    <li>when no case execution is found for the given case execution id or</li>
   *    <li>when the transition is not allowed to be done or</li>
   *    <li>when the case execution is a case instance</li>
   *  </ul>
   */
  void manualStart();

  /**
   * <p>Additionally to {@link #execute()} the associated case execution will
   * be disabled. Therefore there happens a transition from the state <code>ENABLED</code>
   * to state <code>DISABLED</code>.</p>
   *
   * <p>According to CMMN 1.0 specification the state <code>DISABLED</code> means,
   * that the with the case execution related {@link Stage} or {@link Task} should
   * not be executed in the case instance.</p>
   *
   * @throws ProcessEngineException this exception will be thrown
   *  <ul>
   *    <li>when the passed case execution id is null or</li>
   *    <li>when no case execution is found for the given case execution id or</li>
   *    <li>when the transition is not allowed to be done or</li>
   *    <li>when the case execution is a case instance</li>
   *  </ul>
   */
  void disable();

  /**
   * <p>Additionally to {@link #execute()} the associated case execution will
   * be re-enabled. Therefore there happens a transition from the state <code>DISABLED</code>
   * to state <code>ENABLED</code>.</p>
   *
   * <p>According to CMMN 1.0 specification the state <code>DISABLED</code> means,
   * that the with the case execution related {@link Stage} or {@link Task} is waiting
   * for a decision to become <code>ACTIVE</code> or <code>DISABLED</code> once again.</p>
   *
   * @throws ProcessEngineException this exception will be thrown
   *  <ul>
   *    <li>when the passed case execution id is null or</li>
   *    <li>when no case execution is found for the given case execution id or</li>
   *    <li>when the transition is not allowed to be done or</li>
   *    <li>when the case execution is a case instance</li>
   *  </ul>
   */
  void reenable();

}
