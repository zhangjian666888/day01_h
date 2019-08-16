package com.zj.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

@Api(tags = "这是一个FastDfs的客户端的配置。")
@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx重复注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClient {
}
