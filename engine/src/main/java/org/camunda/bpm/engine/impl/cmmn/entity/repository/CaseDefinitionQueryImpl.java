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
package org.camunda.bpm.engine.impl.cmmn.entity.repository;

import java.util.List;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.AbstractQuery;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.CaseDefinitionQuery;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;

/**
 * @author Roman Smirnov
 *
 */
public class CaseDefinitionQueryImpl extends AbstractQuery<CaseDefinitionQuery, CaseDefinition> implements CaseDefinitionQuery {

  private static final long serialVersionUID = 1L;

  protected String id;
  protected String category;
  protected String categoryLike;
  protected String name;
  protected String nameLike;
  protected String deploymentId;
  protected String key;
  protected String keyLike;
  protected String resourceName;
  protected String resourceNameLike;
  protected Integer version;
  protected boolean latest = false;

  public CaseDefinitionQueryImpl() {
  }

  public CaseDefinitionQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }

  public CaseDefinitionQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  // Query parameter //////////////////////////////////////////////////////////////

  public CaseDefinitionQuery caseDefinitionId(String caseDefinitionId) {
    ensureNotNull("caseDefinitionId", caseDefinitionId);
    this.id = caseDefinitionId;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionCategory(String caseDefinitionCategory) {
    ensureNotNull("category", caseDefinitionCategory);
    this.category = caseDefinitionCategory;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionCategoryLike(String caseDefinitionCategoryLike) {
    ensureNotNull("categoryLike", caseDefinitionCategoryLike);
    this.categoryLike = caseDefinitionCategoryLike;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionName(String caseDefinitionName) {
    ensureNotNull("name", caseDefinitionName);
    this.name = caseDefinitionName;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionNameLike(String caseDefinitionNameLike) {
    ensureNotNull("nameLike", caseDefinitionNameLike);
    this.nameLike = caseDefinitionNameLike;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionKey(String caseDefinitionKey) {
    ensureNotNull("key", caseDefinitionKey);
    this.key = caseDefinitionKey;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionKeyLike(String caseDefinitionKeyLike) {
    ensureNotNull("keyLike", caseDefinitionKeyLike);
    this.keyLike = caseDefinitionKeyLike;
    return this;
  }

  public CaseDefinitionQuery deploymentId(String deploymentId) {
    ensureNotNull("deploymentId", deploymentId);
    this.deploymentId = deploymentId;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionVersion(Integer caseDefinitionVersion) {
    ensureNotNull("version", caseDefinitionVersion);
    if (caseDefinitionVersion <= 0) {
      throw new ProcessEngineException("version must be positive");
    }
    this.version = caseDefinitionVersion;
    return this;
  }

  public CaseDefinitionQuery latestVersion() {
    this.latest = true;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionResourceName(String resourceName) {
    ensureNotNull("resourceName", resourceName);
    this.resourceName = resourceName;
    return this;
  }

  public CaseDefinitionQuery caseDefinitionResourceNameLike(String resourceNameLike) {
    ensureNotNull("resourceNameLike", resourceNameLike);
    this.resourceNameLike = resourceNameLike;
    return this;
  }

  public CaseDefinitionQuery orderByCaseDefinitionCategory() {
    orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_CATEGORY);
    return this;
  }

  public CaseDefinitionQuery orderByCaseDefinitionKey() {
    orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_KEY);
    return this;
  }

  public CaseDefinitionQuery orderByCaseDefinitionId() {
    orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_ID);
    return this;
  }

  public CaseDefinitionQuery orderByCaseDefinitionVersion() {
    orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_VERSION);
    return this;
  }

  public CaseDefinitionQuery orderByCaseDefinitionName() {
    orderBy(CaseDefinitionQueryProperty.CASE_DEFINITION_NAME);
    return this;
  }

  public CaseDefinitionQuery orderByDeploymentId() {
    orderBy(CaseDefinitionQueryProperty.DEPLOYMENT_ID);
    return this;
  }

  //results ////////////////////////////////////////////

  public long executeCount(CommandContext commandContext) {
    checkQueryOk();
    return commandContext
      .getCaseDefinitionManager()
      .findCaseDefinitionCountByQueryCriteria(this);
  }

  public List<CaseDefinition> executeList(CommandContext commandContext, Page page) {
    checkQueryOk();
    return commandContext
      .getCaseDefinitionManager()
      .findCaseDefinitionsByQueryCriteria(this, page);
  }

  public void checkQueryOk() {
    super.checkQueryOk();

    // latest() makes only sense when used with key() or keyLike()
    if (latest && ( (id != null) || (name != null) || (nameLike != null) || (version != null) || (deploymentId != null) ) ){
      throw new ProcessEngineException("Calling latest() can only be used in combination with key(String) and keyLike(String)");
    }
  }

  // getters ////////////////////////////////////////////

  public String getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

  public String getCategoryLike() {
    return categoryLike;
  }

  public String getName() {
    return name;
  }

  public String getNameLike() {
    return nameLike;
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public String getKey() {
    return key;
  }

  public String getKeyLike() {
    return keyLike;
  }

  public String getResourceName() {
    return resourceName;
  }

  public String getResourceNameLike() {
    return resourceNameLike;
  }

  public Integer getVersion() {
    return version;
  }

  public boolean isLatest() {
    return latest;
  }

}
