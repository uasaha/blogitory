package com.blogitory.blog.commons.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.BasicType;
import org.hibernate.type.StandardBasicTypes;

/**
 * Match Function Contributor for MySQL 8.
 *
 * @author woonseok
 * @Date 2024-09-03
 * @since 1.0
 **/
public class MatchFunctionContributor implements FunctionContributor {
  @Override
  public void contributeFunctions(FunctionContributions functionContributions) {
    BasicType<Double> resultType = functionContributions
            .getTypeConfiguration()
            .getBasicTypeRegistry()
            .resolve(StandardBasicTypes.DOUBLE);

    functionContributions.getFunctionRegistry()
            .registerPattern("match", "match(?1, ?2) against (?3 in boolean mode)", resultType);
  }
}
