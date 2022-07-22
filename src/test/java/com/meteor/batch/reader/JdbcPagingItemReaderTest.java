package com.meteor.batch.reader;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

import com.meteor.batch.reader.vo.JdbcReaderItem;

@SpringBootTest
public class JdbcPagingItemReaderTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void afterPropertiesSetNotCallTest() throws Exception {
        final JdbcPagingItemReader<JdbcReaderItem> jdbcPagingItemReader =
                new JdbcPagingItemReaderBuilder<JdbcReaderItem>().name("JdbcPagingItemReader")
                                                                 .queryProvider(queryProvider())
                                                                 .dataSource(dataSource)
                                                                 .rowMapper(
                                                                         (rs, idx) -> JdbcReaderItem.builder()
                                                                                                    .build()
                                                                 )
                                                                 .build();
//        jdbcPagingItemReader.afterPropertiesSet();

        ExecutionContext executionContext = new ExecutionContext();
        jdbcPagingItemReader.open(executionContext);

        //java.lang.NullPointerException: Cannot invoke "org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate.getJdbcOperations()" because "this.namedParameterJdbcTemplate" is null
        Assertions.assertThrows(NullPointerException.class, () -> {
            jdbcPagingItemReader.read();
        });
    }

    @Test
    void notExistTableTest() throws Exception {
        System.out.println("ds : " + dataSource);
        final JdbcPagingItemReader<JdbcReaderItem> jdbcPagingItemReader =
                new JdbcPagingItemReaderBuilder<JdbcReaderItem>().name("JdbcPagingItemReader")
                                                                 .queryProvider(queryProvider())
                                                                 .dataSource(dataSource)
                                                                 .rowMapper(
                                                                         (rs, idx) -> JdbcReaderItem.builder()
                                                                                                    .build()
                                                                 )
                                                                 .build();
        jdbcPagingItemReader.afterPropertiesSet();

        ExecutionContext executionContext = new ExecutionContext();
        jdbcPagingItemReader.open(executionContext);
        //org.springframework.jdbc.BadSqlGrammarException: StatementCallback; bad SQL grammar [SELECT TOP 10 empno, ename FROM Emp ORDER BY empno ASC]; nested exception is org.h2.jdbc.JdbcSQLSyntaxErrorException: Table "EMP" not found; SQL statement:
        //SELECT TOP 10 empno, ename FROM Emp ORDER BY empno ASC [42102-200]
        Assertions.assertThrows(BadSqlGrammarException.class, () -> {
            jdbcPagingItemReader.read();
        });
    }

    public PagingQueryProvider queryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean =
                new SqlPagingQueryProviderFactoryBean();
        sqlPagingQueryProviderFactoryBean.setDataSource(dataSource);
        sqlPagingQueryProviderFactoryBean.setSelectClause("empno, ename");
        sqlPagingQueryProviderFactoryBean.setFromClause("Emp");
        sqlPagingQueryProviderFactoryBean.setSortKey("empno");
        return sqlPagingQueryProviderFactoryBean.getObject();
    }

}
