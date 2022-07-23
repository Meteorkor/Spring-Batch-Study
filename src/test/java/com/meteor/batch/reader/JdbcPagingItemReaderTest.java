package com.meteor.batch.reader;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.meteor.batch.reader.vo.JdbcReaderItem;

@SpringBootTest
public class JdbcPagingItemReaderTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private static boolean beforeAllInit = false;

    @BeforeEach
    void setUp() {
        if (!beforeAllInit) {
            jdbcTemplate.execute("create table IF NOT EXISTS `Emp`\n"
                                 + "(\n"
                                 + "  empno bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                                 + "  ename VARCHAR(40) NOT NULL"
                                 + "  );");
            beforeAllInit = true;
        } else {
            jdbcTemplate.update("delete from Emp");
        }
    }

    @Test
    void noReadData() throws Exception {
        final JdbcPagingItemReader<JdbcReaderItem> jdbcPagingItemReader =
                new JdbcPagingItemReaderBuilder<JdbcReaderItem>()
                        .name("JdbcPagingItemReader")
                        .queryProvider(queryProvider())
                        .dataSource(dataSource)
                        .rowMapper(
                                (rs, idx) -> JdbcReaderItem.builder()
                                                           .name(rs.getString("ename"))
                                                           .data(rs.getString("empno"))
                                                           .build()
                        )
                        .build();
        jdbcPagingItemReader.afterPropertiesSet();

        ExecutionContext executionContext = new ExecutionContext();
        jdbcPagingItemReader.open(executionContext);
        final JdbcReaderItem read = jdbcPagingItemReader.read();
        Assertions.assertNull(read);
    }

    @Test
    void readData() throws Exception {
        jdbcTemplate.update("insert into Emp(ename) values('kim') ");
        final JdbcPagingItemReader<JdbcReaderItem> jdbcPagingItemReader =
                new JdbcPagingItemReaderBuilder<JdbcReaderItem>()
                        .name("JdbcPagingItemReader")
                        .queryProvider(queryProvider())
                        .dataSource(dataSource)
                        .rowMapper(
                                (rs, idx) -> JdbcReaderItem.builder()
                                                           .name(rs.getString("ename"))
                                                           .data(rs.getString("empno"))
                                                           .build()
                        )
                        .build();
        jdbcPagingItemReader.afterPropertiesSet();

        ExecutionContext executionContext = new ExecutionContext();
        jdbcPagingItemReader.open(executionContext);
        final JdbcReaderItem read = jdbcPagingItemReader.read();
        Assertions.assertEquals("1", read.getData());
        Assertions.assertEquals("kim", read.getName());
    }

    @Test
    void maxItemCountTest() throws Exception {
        final int pageSize = 100;
        for (int i = 0; i < pageSize * 10; i++) {
            jdbcTemplate.update(String.format("insert into Emp(ename) values('%s') ", "kim" + i));
        }

        final JdbcPagingItemReader<JdbcReaderItem> jdbcPagingItemReader =
                new JdbcPagingItemReaderBuilder<JdbcReaderItem>()
                        .name("JdbcPagingItemReader")
                        .queryProvider(queryProvider())
                        .maxItemCount(pageSize)
                        .dataSource(dataSource)
                        .rowMapper(
                                (rs, idx) -> JdbcReaderItem.builder()
                                                           .name(rs.getString("ename"))
                                                           .data(rs.getString("empno"))
                                                           .build()
                        )
                        .build();
        jdbcPagingItemReader.afterPropertiesSet();

        ExecutionContext executionContext = new ExecutionContext();
        jdbcPagingItemReader.open(executionContext);

        for (int i = 0; i < pageSize; i++) {
            final JdbcReaderItem read = jdbcPagingItemReader.read();
            Assertions.assertNotNull(read);
        }

        final JdbcReaderItem read = jdbcPagingItemReader.read();
        Assertions.assertNull(read);
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
