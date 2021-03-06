/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.schema.property;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.schema.AlternateTypeProvider;

import static com.google.common.base.Optional.fromNullable;
import static springfox.documentation.schema.ResolvedTypes.simpleQualifiedTypeName;

public abstract class BaseModelProperty implements ModelProperty {

  private final String name;
  private final AlternateTypeProvider alternateTypeProvider;

  public BaseModelProperty(String name, AlternateTypeProvider alternateTypeProvider) {
    this.name = name;
    this.alternateTypeProvider = alternateTypeProvider;
  }

  protected abstract ResolvedType realType();

  @Override
  public ResolvedType getType() {
    return alternateTypeProvider.alternateFor(realType());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String qualifiedTypeName() {
    if (getType().getTypeParameters().size() > 0) {
      return getType().toString();
    }
    return simpleQualifiedTypeName(getType());
  }

  @Override
  public AllowableValues allowableValues() {
    Optional<AllowableValues> allowableValues = fromNullable(ResolvedTypes.allowableValues(getType()));
    //Preference to inferred allowable values over list values via ApiModelProperty
    if (allowableValues.isPresent()) {
      return allowableValues.get();
    }
    return null;
  }

  @Override
  public boolean isRequired() {
    return false;
  }


  @Override
  public String propertyDescription() {
    return null;
  }

  @Override
  public int position() {
    return 0;
  }
}
