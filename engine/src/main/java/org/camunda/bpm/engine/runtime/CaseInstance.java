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

/**
 * @author Roman Smirnov
 *
 */
public interface CaseInstance extends CaseExecution {

  /**
   * The id of the case definition of the case instance.
   */
  String getCaseDefinitionId();

  /**
   * The business key of this process instance.
   */
  String getBusinessKey();

  /**
   * <p>Returns <code>true</code> if the case instance is completed.</p>
   *
   * <p><strong>Note:</strong> If this case execution is not the case instance,
   * it will always return <code>false</code>.</p>
   */
  boolean isCompleted();

}
