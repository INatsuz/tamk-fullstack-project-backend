package com.vraminhos.backend.graphql;

import graphql.schema.GraphQLScalarType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfigurer {

	@Autowired
	private GraphQLScalarType dateScalar;

	@Bean
	RuntimeWiringConfigurer configurer() {
		GraphQLScalarType dateScalarType = dateScalar;
		return (builder) -> builder.scalar(dateScalarType);
	}

}
