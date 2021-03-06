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

package org.camunda.bpm.engine.impl.pvm.runtime.operation;

import java.util.List;

import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;


/**
 * @author Tom Baeyens
 */
public class PvmAtomicOperationDeleteCascade implements PvmAtomicOperation {
  
  public boolean isAsync(PvmExecutionImpl execution) {
    return false;
  }

  public void execute(PvmExecutionImpl execution) {
     PvmExecutionImpl firstLeaf = findFirstLeaf(execution);
    
    if (firstLeaf.getSubProcessInstance()!=null) {
      firstLeaf.getSubProcessInstance().deleteCascade(execution.getDeleteReason());
    }

    firstLeaf.performOperation(DELETE_CASCADE_FIRE_ACTIVITY_END);
  }

  @SuppressWarnings("unchecked")
  protected PvmExecutionImpl findFirstLeaf(PvmExecutionImpl execution) {
    List<? extends PvmExecutionImpl> executions = execution.getExecutions();
    if (executions.size()>0) {
      return findFirstLeaf(executions.get(0));
    }
    return execution;
  }

  public String getCanonicalName() {
    return "delete-cascade";
  }
}
