package com.example.petlifecycle.ai.service;

import com.example.petlifecycle.ai.dto.AiResponse;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    public AiResponse getAnswer(Long reportId, String question) {
        // reportId를 사용할 수 있지만, 지금은 질문 내용만으로 답변을 생성합니다.
        String answer;
        if (question.contains("영양소") || question.contains("부족")) {
            answer = "리포트 결과에 따르면, 현재 반려동물은 비타민 D와 오메가-3가 부족할 수 있습니다. 수의사와 상담하여 영양제 급여를 고려해보세요.";
        } else if (question.contains("활동") || question.contains("운동")) {
            answer = "현재 체중을 고려할 때, 하루 30분 이상의 산책과 주 2회 이상의 인터랙티브 놀이(예: 공 던지기, 낚싯대 놀이)를 추천합니다.";
        } else if (question.contains("비만") || question.contains("체중")) {
            answer = "현재 체중은 정상 범위에서 약간 높은 편입니다. 꾸준한 활동과 함께 사료 양을 10% 정도 줄여보는 것을 권장합니다. 정확한 진단은 수의사와 상의하세요.";
        } else {
            answer = "죄송합니다. 질문을 이해하지 못했어요. 리포트와 관련된 다른 질문을 해주시겠어요?";
        }

        return new AiResponse(answer);
    }
}
