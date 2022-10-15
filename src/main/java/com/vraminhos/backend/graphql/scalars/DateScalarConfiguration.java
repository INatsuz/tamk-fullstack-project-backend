package com.vraminhos.backend.graphql.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateScalarConfiguration {

	@Bean
	public GraphQLScalarType dateScalar() {
		return GraphQLScalarType.newScalar()
				.name("Date")
				.description("java.util.Date as a Scalar type")
				.coercing(new Coercing<Date, String>() {

					@Override
					public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
						if (dataFetcherResult instanceof Date) {
							// Converting Date object to a UTC ISO formatted String
							TimeZone tz = TimeZone.getTimeZone("UTC");
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
							df.setTimeZone(tz);

							return df.format((Date) dataFetcherResult);
						} else {
							throw new CoercingSerializeException("Expected a Date");
						}
					}

					@Override
					public Date parseValue(Object input) throws CoercingParseValueException {
						if (input instanceof String) {
							TimeZone tz = TimeZone.getTimeZone("UTC");
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
							df.setTimeZone(tz);

							try {
								return df.parse((String) input);
							} catch (ParseException e) {
								throw new CoercingParseValueException("Could not parse String to Date scalar");
							}
						} else {
							throw new CoercingParseValueException("Expected a String input");
						}
					}

					@Override
					public Date parseLiteral(Object input) throws CoercingParseLiteralException {
						if (input instanceof StringValue) {
							TimeZone tz = TimeZone.getTimeZone("UTC");
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
							df.setTimeZone(tz);

							try {
								return df.parse(((StringValue) input).getValue());
							} catch (ParseException e) {
								throw new CoercingParseValueException("Could not parse literal to Date scalar");
							}
						} else {
							throw new CoercingParseLiteralException("Expected a literal");
						}
					}
				}).build();
	}

}
