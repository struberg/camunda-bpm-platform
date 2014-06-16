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
package org.camunda.bpm.engine.impl.cmmn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.cmmn.cmd.CaseExecutionVariableCmd;
import org.camunda.bpm.engine.impl.cmmn.cmd.DisableCaseExecutionCmd;
import org.camunda.bpm.engine.impl.cmmn.cmd.ManualStartCaseExecutionCmd;
import org.camunda.bpm.engine.impl.cmmn.cmd.ReenableCaseExecutionCmd;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.runtime.CaseExecutionCommandBuilder;

/**
 * @author Roman Smirnov
 *
 */
public class CaseExecutionCommandBuilderImpl implements CaseExecutionCommandBuilder {

  protected CommandExecutor commandExecutor;
  protected CommandContext commandContext;

  protected String caseExecutionId;
  protected Map<String, Object> variables;
  protected Map<String, Object> variablesLocal;
  protected Collection<String> variableDeletions;
  protected Collection<String> variableLocalDeletions;

  public CaseExecutionCommandBuilderImpl(CommandExecutor commandExecutor, String caseExecutionId) {
    this(caseExecutionId);
    if(commandExecutor == null) {
      throw new ProcessEngineException("commandExecutor cannot be null");
    }
    this.commandExecutor = commandExecutor;
  }

  public CaseExecutionCommandBuilderImpl(CommandContext commandContext, String caseExecutionId) {
    this(caseExecutionId);
    if(commandContext == null) {
      throw new ProcessEngineException("commandContext cannot be null");
    }
    this.commandContext = commandContext;
  }

  private CaseExecutionCommandBuilderImpl(String caseExecutionId) {
    this.caseExecutionId = caseExecutionId;
  }

  public CaseExecutionCommandBuilder setVariable(String variableName, Object variableValue) {
    if (variables == null) {
      variables = new HashMap<String, Object>();
    }
    variables.put(variableName, variableValue);
    return this;
  }

  public CaseExecutionCommandBuilder setVariables(Map<String, Object> variables) {
    if (this.variables == null) {
      this.variables = new HashMap<String, Object>();
    }
    this.variables.putAll(variables);
    return this;
  }

  public CaseExecutionCommandBuilder setVariableLocal(String localVariableName, Object localVariableValue) {
    if (variablesLocal == null) {
      variablesLocal = new HashMap<String, Object>();
    }
    variablesLocal.put(localVariableName, localVariableValue);
    return this;
  }

  public CaseExecutionCommandBuilder setVariablesLocal(Map<String, Object> variablesLocal) {
    if (this.variablesLocal == null) {
      this.variablesLocal = new HashMap<String, Object>();
    }
    this.variablesLocal.putAll(variablesLocal);
    return this;
  }

  public CaseExecutionCommandBuilder removeVariable(String variableName) {
    if (variableDeletions == null) {
      variableDeletions = new ArrayList<String>();
    }
    variableDeletions.add(variableName);
    return this;
  }

  public CaseExecutionCommandBuilder removeVariables(Collection<String> variableNames) {
    if (variableDeletions == null) {
      variableDeletions = new ArrayList<String>();
    }
    variableDeletions.addAll(variableNames);
    return this;
  }

  public CaseExecutionCommandBuilder removeVariableLocal(String variableName) {
    if (variableLocalDeletions == null) {
      variableLocalDeletions = new ArrayList<String>();
    }
    variableLocalDeletions.add(variableName);
    return this;
  }

  public CaseExecutionCommandBuilder removeVariablesLocal(Collection<String> variableNames) {
    if (variableLocalDeletions == null) {
      variableLocalDeletions = new ArrayList<String>();
    }
    variableLocalDeletions.addAll(variableNames);
    return this;
  }

  public void execute() {
    CaseExecutionVariableCmd command = new CaseExecutionVariableCmd(this);
    executeCommand(command);
  }

  public void manualStart() {
    ManualStartCaseExecutionCmd command = new ManualStartCaseExecutionCmd(this);
    executeCommand(command);
  }

  public void disable() {
    DisableCaseExecutionCmd command = new DisableCaseExecutionCmd(this);
    executeCommand(command);
  }

  public void reenable() {
    ReenableCaseExecutionCmd command = new ReenableCaseExecutionCmd(this);
    executeCommand(command);
  }

  protected void executeCommand(Command<?> command) {
    if(commandExecutor != null) {
      commandExecutor.execute(command);
    } else {
      command.execute(commandContext);
    }
  }

  // getters ////////////////////////////////////////////////////////////////////////////////

  public String getCaseExecutionId() {
    return caseExecutionId;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  public Map<String, Object> getVariablesLocal() {
    return variablesLocal;
  }

  public Collection<String> getVariableDeletions() {
    return variableDeletions;
  }

  public Collection<String> getVariableLocalDeletions() {
    return variableLocalDeletions;
  }

}
