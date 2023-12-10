package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.*;
import com.ptit.hackerthonservice.entity.ExamExercise;
import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import com.ptit.hackerthonservice.ws.client.RemoteCompilerService;
import com.ptit.hackerthonservice.ws.dto.Request;
import com.ptit.hackerthonservice.ws.dto.Response;
import com.ptit.hackerthonservice.ws.dto.TestCase;
import com.ptit.hackerthonservice.ws.dto.TestCaseResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public interface CheckCodeService {

    AnswerDTO runCode(RunCodeDTO runCodeDTO);

    AnswerDTO checkCode(RunCodeDTO runCodeDTO);

    AnswerDTO submitCode(RunCodeDTO runCodeDTO);


    AnswerDTO submitExam(RunCodeDTO runCodeDTO, Principal principal);
}

@Service
class CheckCodeServiceImpl implements CheckCodeService {

    @Autowired
    RemoteCompilerService remoteCompilerService;

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    AnswerService answerService;

    @Autowired
    RankingExamService rankingExamService;

    @Autowired
    ExerciseRepo exerciseRepo;

    @Value("${compiler.apikey:123456789}")
    private String API_KEY;

    @Override
    public AnswerDTO runCode(RunCodeDTO runCodeDTO) {
        Map<String, TestCase> testCasesRequest = new HashMap<>();
        TestCase testCase = new TestCase();
        testCase.setInput(runCodeDTO.getInput());
        testCase.setExpectedOutput("");
        testCasesRequest.put("test1", testCase);

        Request request = new ModelMapper().map(runCodeDTO, Request.class);
        request.setTestCases(testCasesRequest);
        request.setMemoryLimit(500);
        request.setTimeLimit(10);
        Response response = remoteCompilerService.compile(API_KEY, request);

        AnswerDTO answerDTO = new ModelMapper().map(response, AnswerDTO.class);
        answerDTO.setTestCasesResults(response.getTestCasesResult().values().stream().map(tc -> new ModelMapper().map(tc, TestCaseResulTDTO.class)).collect(Collectors.toList()));

        return answerDTO;
    }

    @Override
    public AnswerDTO checkCode(RunCodeDTO runCodeDTO) {
        Map<String, TestCase> testCasesRequest = new HashMap<>();

        for (int i = 0; i < runCodeDTO.getTestCases().size(); i++) {
            TestCase testCase = new ModelMapper().map(runCodeDTO.getTestCases().get(i), TestCase.class);
            testCasesRequest.put(String.valueOf(i), testCase);
        }

        Request request = new ModelMapper().map(runCodeDTO, Request.class);
        request.setTestCases(testCasesRequest);
        request.setMemoryLimit(500);
        request.setTimeLimit(10);

        Response response = remoteCompilerService.compile(API_KEY, request);

        AnswerDTO answerDTO = new ModelMapper().map(response, AnswerDTO.class);

        List<TestCaseResulTDTO> testCaseResulTDTOs = new ArrayList<>();
        for (Entry<String, TestCaseResult> entry : response.getTestCasesResult().entrySet()) {
            TestCaseResulTDTO testCaseResulTDTO = new ModelMapper().map(entry.getValue(), TestCaseResulTDTO.class);
            testCaseResulTDTO.setInput(testCasesRequest.get(entry.getKey()).getInput());
            testCaseResulTDTOs.add(testCaseResulTDTO);
        }
        answerDTO.setTestCasesResults(testCaseResulTDTOs);

        return answerDTO;
    }

    @Override
    public AnswerDTO submitCode(RunCodeDTO runCodeDTO) {
        Exercise exercises = exerciseRepo.findById(runCodeDTO.getExerciseId()).orElseThrow();

        ExerciseDTO exercise= new ModelMapper().map(exercises,ExerciseDTO.class);

        Map<String, TestCase> testCasesRequest = new HashMap<>();

        for (int i = 0; i < exercise.getTestCases().size(); i++) {
            TestCase testCase = new ModelMapper().map(exercise.getTestCases().get(i), TestCase.class);
            testCasesRequest.put(String.valueOf(i), testCase);
        }

        Request request = new ModelMapper().map(runCodeDTO, Request.class);
        request.setTestCases(testCasesRequest);

        request.setMemoryLimit(exercise.getMemoryLimit());
        request.setTimeLimit(10);


        Response response = remoteCompilerService.compile(API_KEY, request);

        AnswerDTO answerDTO = new ModelMapper().map(response, AnswerDTO.class);

        List<TestCaseResulTDTO> testCaseResulTDTOs = new ArrayList<>();

        int score = 0;
        for (Entry<String, TestCaseResult> entry : response.getTestCasesResult().entrySet()) {
            TestCaseResulTDTO testCaseResulTDTO = new ModelMapper().map(entry.getValue(), TestCaseResulTDTO.class);
            testCaseResulTDTO.setInput(testCasesRequest.get(entry.getKey()).getInput());

            if (testCaseResulTDTO.getVerdictStatusCode() == 100) {
                testCaseResulTDTO.setScore(testCasesRequest.get(entry.getKey()).getScore());
                score += testCaseResulTDTO.getScore();
            }
            testCaseResulTDTOs.add(testCaseResulTDTO);
        }
        answerDTO.setTestCasesResults(testCaseResulTDTOs);
        answerDTO.setScore(score);
        answerDTO.setSourceCode(runCodeDTO.getSourcecode());
        answerDTO.setExercise(exercise);

       answerService.create(answerDTO);
        // hide test case for view
        answerDTO.getTestCasesResults().stream().forEach(tc -> {
            tc.setInput("...");
            tc.setOutput("...");
            tc.setExpectedOutput("...");
        });

        return answerDTO;

    }

    @Override
    public AnswerDTO submitExam(RunCodeDTO runCodeDTO, Principal principal) {
        Exercise exercises = exerciseRepo.findById(runCodeDTO.getExerciseId()).orElseThrow();

        ExerciseDTO exercise= new ModelMapper().map(exercises,ExerciseDTO.class);

        Map<String, TestCase> testCasesRequest = new HashMap<>();

        for (int i = 0; i < exercise.getTestCases().size(); i++) {
            TestCase testCase = new ModelMapper().map(exercise.getTestCases().get(i), TestCase.class);
            testCasesRequest.put(String.valueOf(i), testCase);
        }

        Request request = new ModelMapper().map(runCodeDTO, Request.class);
        request.setTestCases(testCasesRequest);

        request.setMemoryLimit(exercise.getMemoryLimit());
        request.setTimeLimit(10);


        Response response = remoteCompilerService.compile(API_KEY, request);

        AnswerDTO answerDTO = new ModelMapper().map(response, AnswerDTO.class);

        List<TestCaseResulTDTO> testCaseResulTDTOs = new ArrayList<>();

        int score = 0;
        for (Entry<String, TestCaseResult> entry : response.getTestCasesResult().entrySet()) {
            TestCaseResulTDTO testCaseResulTDTO = new ModelMapper().map(entry.getValue(), TestCaseResulTDTO.class);
            testCaseResulTDTO.setInput(testCasesRequest.get(entry.getKey()).getInput());

            if (testCaseResulTDTO.getVerdictStatusCode() == 100) {
                testCaseResulTDTO.setScore(testCasesRequest.get(entry.getKey()).getScore());
                score += testCaseResulTDTO.getScore();
            }
            testCaseResulTDTOs.add(testCaseResulTDTO);
        }

        RankingExamDTO rankingExamDTO=rankingExamService.check(Integer.valueOf(principal.getName()));
        if(rankingExamDTO !=null){
            AnswerDTO answerDTO1= answerService.check(Integer.valueOf(principal.getName()),runCodeDTO.getExerciseId());
            if(answerDTO1 == null){
                rankingExamDTO.setScore(rankingExamDTO.getScore()+score);
                rankingExamService.update(rankingExamDTO);
            }

        }else{
            RankingExamDTO rankingExamDTO1= new RankingExamDTO();
            rankingExamDTO1.setExam(runCodeDTO.getExam());
            rankingExamDTO1.setScore(Long.valueOf(score));
            rankingExamService.create(rankingExamDTO1);
        }
        answerDTO.setTestCasesResults(testCaseResulTDTOs);
        answerDTO.setScore(score);
        answerDTO.setSourceCode(runCodeDTO.getSourcecode());
        answerDTO.setExercise(exercise);

        answerService.create(answerDTO);

        // hide test case for view
        answerDTO.getTestCasesResults().stream().forEach(tc -> {
            tc.setInput("...");
            tc.setOutput("...");
            tc.setExpectedOutput("...");
        });

        return answerDTO;

    }
}
