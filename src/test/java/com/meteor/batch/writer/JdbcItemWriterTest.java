package com.meteor.batch.writer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.meteor.batch.reader.vo.Emp;
import com.meteor.batch.reader.vo.JdbcReaderItem;

@SpringBootTest
public class JdbcItemWriterTest {

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
    void writeTest() throws Exception {
        final JdbcBatchItemWriter<JdbcReaderItem> jdbcBatchItemWriter =
                new JdbcBatchItemWriterBuilder<JdbcReaderItem>()
                        .sql("insert into Emp(ename) values(?)")
                        .dataSource(dataSource)
                        .itemPreparedStatementSetter((JdbcReaderItem item, PreparedStatement ps) -> {
                            ps.setString(1, item.getName());
                        }).build();
        jdbcBatchItemWriter.afterPropertiesSet();

        final ArrayList<JdbcReaderItem> list = Lists.newArrayList(JdbcReaderItem.builder()
                                                                                .name("kim")
                                                                                .build());

        jdbcBatchItemWriter.write(list);

        final List<Emp> result = jdbcTemplate.query("select * from Emp", (ResultSet var1, int var2) -> {
            return Emp.builder().empno(var1.getInt(1)).ename(var1.getString(2)).build();
        });

        Emp emp = result.get(0);

        Assertions.assertEquals("kim", emp.getEname());
    }

    @Test
    void multiWriteTest() throws Exception {
        final int DATA_CNT = 100;
        final JdbcBatchItemWriter<JdbcReaderItem> jdbcBatchItemWriter =
                new JdbcBatchItemWriterBuilder<JdbcReaderItem>()
                        .sql("insert into Emp(ename) values(?)")
                        .dataSource(dataSource)
                        .itemPreparedStatementSetter((JdbcReaderItem item, PreparedStatement ps) -> {
                            ps.setString(1, item.getName());
                        })
                        .build();
        jdbcBatchItemWriter.afterPropertiesSet();

        final List<JdbcReaderItem> list = IntStream.range(0, DATA_CNT)
                                                   .mapToObj(n -> JdbcReaderItem.builder()
                                                                                .name("kim" + n)
                                                                                .build())
                                                   .collect(Collectors.toList());

        jdbcBatchItemWriter.write(list);

        final List<Emp> result = jdbcTemplate.query("select * from Emp", (ResultSet var1, int var2) -> {
            return Emp.builder().empno(var1.getInt(1)).ename(var1.getString(2)).build();
        });

        Emp emp = result.get(0);
        Assertions.assertEquals("kim0", emp.getEname());

        Assertions.assertEquals(DATA_CNT, result.size());

    }

    @Test
    void namedItemSqlParameterSourceProviderWriteTest() throws Exception {
        final int DATA_CNT = 100;
        final JdbcBatchItemWriter<JdbcReaderItem> jdbcBatchItemWriter =
                new JdbcBatchItemWriterBuilder<JdbcReaderItem>()
                        .sql("insert into Emp(ename) values(:ename)")
                        .dataSource(dataSource)
                        .itemSqlParameterSourceProvider(item -> {
                            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
                            mapSqlParameterSource.addValue("ename", item.getName());
                            return mapSqlParameterSource;
                        })
                        .build();
        jdbcBatchItemWriter.afterPropertiesSet();

        final List<JdbcReaderItem> list = IntStream.range(0, DATA_CNT)
                                                   .mapToObj(n -> JdbcReaderItem.builder()
                                                                                .name("kim" + n)
                                                                                .build())
                                                   .collect(Collectors.toList());

        jdbcBatchItemWriter.write(list);

        final List<Emp> result = jdbcTemplate.query("select * from Emp", (ResultSet var1, int var2) -> {
            return Emp.builder().empno(var1.getInt(1)).ename(var1.getString(2)).build();
        });

        Emp emp = result.get(0);
        Assertions.assertEquals("kim0", emp.getEname());

        Assertions.assertEquals(DATA_CNT, result.size());

    }

    @Test
    void namedItemPreparedStatementSetterFailWriteTest() throws Exception {
        final int DATA_CNT = 100;
        final JdbcBatchItemWriter<JdbcReaderItem> jdbcBatchItemWriter =
                new JdbcBatchItemWriterBuilder<JdbcReaderItem>()
                        .sql("insert into Emp(ename) values(:ename)")
                        .dataSource(dataSource)
//                        .itemSqlParameterSourceProvider(item -> {
//                            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
//                            mapSqlParameterSource.addValue("ename", item.getName());
//                            return mapSqlParameterSource;
//                        })
                        .itemPreparedStatementSetter((JdbcReaderItem item, PreparedStatement ps) -> {
                            ps.setString(1, item.getName());
                        })
                        .build();
        jdbcBatchItemWriter.afterPropertiesSet();

        final List<JdbcReaderItem> list = IntStream.range(0, DATA_CNT)
                                                   .mapToObj(n -> JdbcReaderItem.builder()
                                                                                .name("kim" + n)
                                                                                .build())
                                                   .collect(Collectors.toList());

        Assertions.assertThrows(NullPointerException.class, () -> {
            //java.lang.NullPointerException: Cannot invoke "org.springframework.batch.item.database.ItemSqlParameterSourceProvider.createSqlParameterSource(Object)" because "this.itemSqlParameterSourceProvider" is null
            //쿼리내 named
            jdbcBatchItemWriter.write(list);
        });
    }
}
