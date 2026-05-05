package com.shkim.CTR.System;

import com.shkim.CTR.problem.Problem;
import com.shkim.CTR.problem.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class WebClientServiceImpl {
//    public List<Object[]> getObject(int num){
//        StringBuilder sb = new StringBuilder();
//        for (int i=1000+100*num; i<1000+100*(num+1); i++){
//            sb.append(i);
//            if (i < 1000+100*(num+1)-1) sb.append(',');
//        }
//        WebClient webClient = WebClient
//                .builder()
//                .baseUrl("https://solved.ac/api/v3")
//                .build();
//        List<Problem> response = webClient
//                .get()
//                .uri(uriBuilder ->
//                        uriBuilder
//                                .path("/problem/lookup")
//                                .queryParam("problemIds", sb)
//                                .build())
//                .retrieve()
//                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse -> Mono.error(new RuntimeException("429 Too Many Requests")))
//                .bodyToFlux(Problem.class).collectList()
//                .delaySubscription(Duration.ofMillis(1000)) // 요청 간 기본 1초 딜레이
//                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)) // 429 시 최대 3번, 2초 간격 재시도
//                        .filter(throwable -> throwable instanceof RuntimeException &&
//                                throwable.getMessage().contains("429"))).block();
//        return response.stream().map(problem -> new Object[]{problem.problemId(), problem.titleKo(), problem.level()}).toList();
//    }
//    public List<Problem> get(int num) {
//        StringBuilder sb = new StringBuilder();
//        for (int i=1000+100*num; i<1000+100*(num+1); i++){
//            sb.append(i);
//            if (i < 1000+100*(num+1)-1) sb.append(',');
//        }

        // webClient 기본 설정
//        WebClient webClient =
//                WebClient
//                        .builder()
//                        .baseUrl("https://solved.ac/api/v3")
//                        .build();

        // api 요청
//        return WebClient
//                .builder()
//                .baseUrl("https://solved.ac/api/v3")
//                .build()
//                        .get()
//                        .uri(uriBuilder ->
//                                uriBuilder
//                                        .path("/problem/lookup")
//                                        .queryParam("problemIds", sb)
//                                        .build())
//                        .retrieve()
//                        .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse -> Mono.error(new RuntimeException("429 Too Many Requests")))
//                        .bodyToFlux(Problem.class).collectList()
//                        .delaySubscription(Duration.ofMillis(500)) // 요청 간 기본 1초 딜레이
//                        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)) // 429 시 최대 3번, 2초 간격 재시도
//                                .filter(throwable -> throwable instanceof RuntimeException &&
//                                        throwable.getMessage().contains("429"))).block();
//        List<Problem> list = new ArrayList<>();
//        System.out.println("Size: "+response.size());
//        for (int i=0; i< response.size(); i++){
//            list.add(new Problem(Integer.parseInt(response.get(i).get("problemId").toString()), response.get(i).get("titleKo").toString(),
//                    null, false, false,
//                    0, Integer.parseInt(response.get(i).get("level").toString()),
//                    0, false, false, false, 0, false, null));
//        }
        /*
        public record Problem(int problemId, String titleKo, List<Title> titles, boolean isSolvable, boolean isPartial,
                      int acceptedUserCount, int level, int votedUserCount, boolean sprout, boolean givesNoRating,
                      boolean isLevelLocked, double averageTries, boolean official, List<Tags> tags)
         */
        //list.add(new Problem(Integer.parseInt(response.get("problemId").toString()), String.valueOf(response.get("titleKo")), null, false, false, null,
        //        Integer.parseInt(response.get("level").toString()), 0, false, false, false, 0, false, null))
        //return response;
        //log.info(response.get("titleKo").toString());
//    }

//    public Tag getTag(int num) {
//        return WebClient
//                .builder()
//                .baseUrl("https://solved.ac/api/v3")
//                .build()
//                .get()
//                .uri(uriBuilder ->
//                        uriBuilder
//                                .path("/tag/list")
//                                .queryParam("page", num)
//                                .build())
//                .retrieve()
//                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse -> Mono.error(new RuntimeException("429 Too Many Requests")))
//                .bodyToMono(Tag.class)
//                .delaySubscription(Duration.ofMillis(1000)) // 요청 간 기본 1초 딜레이
//                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)) // 429 시 최대 3번, 2초 간격 재시도
//                        .filter(throwable -> throwable instanceof RuntimeException &&
//                                throwable.getMessage().contains("429"))).block();
//    }
}
