package com.ssafyhome.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SWAGGER 이용 및 컨트롤러 이외의 API 명세를 위한 설정 파일
 * 
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Zip-chack",
        description = "# Zip-chack serviec APIs \n\n"
        		+ "## 마음에 드는 집에 착! 집착 서비스 api sample 입니다. \n\n"
        		+ "### 제공 API : \n\n"
        		+ "1. 로그인 및 인증 관련 서비스 \n\n"
        		+ "2. __Bookmark__ 즐겨찾기 서비스 \n\n"
        		+ "3. __House__ 거래확인 및 요약그래프 서비스 \n\n"
        		+ "4. __Navigate__ 매물과 bookmark 거리 확인 서비스 \n\n"
        		+ "5. __Notice__ 공지사항 서비스 \n\n"
        		+ "6. __Review__ 매물 평가 서비스 \n\n"
        		+ "7. __Spot__ 특정 법정동 내 편의시설 확안 서비스 \n\n"
        		+ "8. __User__ 관리 서비스"
        		
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {
	
	
  /**
   * 로그인을 Swagger Docs에 노출 시키기 위한 메서드
   *
   * Login의 경우 Controller에 없고 Spring Security Filter Chain에서 Url확인해서 로그인을 진행하므로
   * controller에 따로 작성을 하지 않음
   * 그래서 일반적으로는  swaager에 노출이 안되는데 따로 설정값을 통해 노출이 가능하다.
   */
  @Bean
  public OpenAPI loginmOpenAPI() {
	  
    OpenAPI openAPI = new OpenAPI();
    /**
     * 태그를 통해서 각 컨트롤러마다 탭을 나눌 수 있다
     * 태그 자체를 추가하는 과정
     */
    openAPI.addTagsItem(new Tag().name("Authentication").description("인증 및 인가"));

    PathItem loginPath = new PathItem();
    
    
    /**
     * Swagger에 들어갈 명령 덩어리 생성하는 과정
     */
    Operation loginOperation = new Operation()
        .addTagsItem("Authentication Controller")
        .summary("로그인 성공 시 JWT 발급")
        .description("### 로그인에 성공하면 사용자에게 JWT를 발급합니다. \n\n"
        		+ "1. Authorization > access token 추가 \n\n "
        		+ "2. Cookie        > refresh token 추가 ")
        .requestBody(new RequestBody()
            .content(new Content()
                .addMediaType("application/x-www-form-urlencoded",
                    new MediaType()
                        .schema(new Schema<>()
                            .type("object")
                            .addProperties("id", new Schema<>().type("string").description("사용자의 아이디"))
                            .addProperties("password", new Schema<>().type("string").description("사용자의 비밀번호"))
                        )
                )
            )
        )
        .responses(new ApiResponses()
            .addApiResponse("200", new ApiResponse()
                .description("로그인 성공")
                .addHeaderObject("Set-Cookie", new Header()
                    .description("Refresh token")
                    .schema(new Schema<>().type("string"))
                )
                .addHeaderObject("Authorization", new Header()
                    .description("Access token")
                    .schema(new Schema<>().type("string"))
                )
                .content(new Content()
                    .addMediaType("application/json",
                        new MediaType()
                            .schema(new Schema<>()
                                .type("object")
                                .addProperties("message", new Schema<>().type("string").example("로그인 성공"))
                            )
                    )
                )
            )
        );

    /**
     * 위에서 작성한 명령덩어리를 해당 Path와 매칭하는 과정
     */
    loginPath.setPost(loginOperation);
    openAPI.path("/auth/login", loginPath);

    return openAPI;
  }
}
