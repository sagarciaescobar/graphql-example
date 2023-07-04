package com.grahql.example;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static graphql.Assert.assertFalse;
import static graphql.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ExampleApplicationTests {

	@Autowired
	DgsQueryExecutor dgsQueryExecutor;

	@Test
	void testOneHello(){
		@Language("GraphQL") String graphqlQuery = """
				{
					oneHello {
						text
						randomInt
					}
				}
				""";
		String text = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQuery, "data.oneHello.text");
		Integer randomInt = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQuery, "data.oneHello.randomInt");

		assertFalse(StringUtils.isBlank(text));
		assertNotNull(randomInt);
	}

	@Test
	void testAllHellos() {
		@Language("GraphQL") String graphqlQuery = """
				{
					allHellos {
						text
						randomInt
					}
				}
				""";
		List<String> texts = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQuery, "data.allHellos[*].text" );
		List<Integer> randomInt = dgsQueryExecutor.executeAndExtractJsonPath(graphqlQuery, "data.allHellos[*].randomInt");

		assertNotNull(texts);
		assertFalse(texts.isEmpty());
		assertNotNull(randomInt);
		assertEquals(texts.size(),randomInt.size());
	}
}
