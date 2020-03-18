package com.cloud.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
   @Bean
   public Docket customDocket(){
      ParameterBuilder par = new ParameterBuilder();
      ParameterBuilder par1 = new ParameterBuilder();
      ParameterBuilder par2 = new ParameterBuilder();
      ParameterBuilder par3 = new ParameterBuilder();
      ParameterBuilder par4 = new ParameterBuilder();

      List<Parameter> pars = new ArrayList<>();
      par.name("requestId").description("请求id 每次请求生成唯一id 可以用uuid生成").modelRef(new ModelRef("String")).parameterType("header").required(false);
      par1.name("timestamp").description("请求时间戳").modelRef(new ModelRef("String")).parameterType("header").required(false);
      par2.name("channelType").description("请求来源类型 例如：ios android h5 web ").modelRef(new ModelRef("String")).parameterType("header").required(false);
      par3.name("sign").description("参数签名 按照约定好的规则进行签名 加密方式md5和RSA 如果在网关过滤器控制是否开启").modelRef(new ModelRef("String")).parameterType("header").required(false);
      par4.name("token").description("用户token 用户登录后会返回").modelRef(new ModelRef("String")).parameterType("header").required(false);
      pars.add(par.build());
      pars.add(par1.build());
      pars.add(par2.build());
      pars.add(par3.build());
      pars.add(par4.build());
      return new Docket(DocumentationType.SWAGGER_2)
           .apiInfo(apiInfo())
           .genericModelSubstitutes(ResponseEntity.class)
           .select()
           //配置仅需要显示api的路径
           .apis(RequestHandlerSelectors.basePackage("com.cloud.user.controller"))
           .build()
           .globalOperationParameters(pars);
             
   }

   private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
              .title("用户中心api接口文档 ***")
              .description("api根地址：http://ip:端口/(网关地址)/api-u/相关接口url；=========" +
                      "header里面定义的参数如下：" +
                      "timestamp:时间戳验证；" +
                      "requestId:保证每次请求都是唯一；" +
                      "sign:签名；" +"token:登录令牌；"+
                      "channelId:渠道来源 ios android h5 web ")
              // .termsOfServiceUrl("")
              .contact(new Contact("蒋文杰 QQ：619594586", "https://github.com/jiangwenjie1989", "619594586@qq.com"))
              .version("0.0.1")
              .build();
   }


}
