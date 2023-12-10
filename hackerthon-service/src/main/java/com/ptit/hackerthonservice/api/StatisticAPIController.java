package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.StatisticDTO;
import com.ptit.hackerthonservice.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistic")
public class StatisticAPIController {
    @Autowired
    StatisticService statisticService;

    @PutMapping("/{id}/like")
    public ResponseDTO<Void> updateLike(@PathVariable(value = "id") long id) {
        statisticService.updateLike(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @PutMapping("/{id}/view")
    public ResponseDTO<Void> updateView(@PathVariable(value = "id") long id) {
        statisticService.updateView(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @GetMapping("/{id}")
    public ResponseDTO<StatisticDTO> get(@PathVariable(value = "id") long id) {
        return ResponseDTO.<StatisticDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
                .data(statisticService.get(id)).build();
    }

    @GetMapping("/")
    public ResponseDTO<StatisticDTO> statistic() {
        return ResponseDTO.<StatisticDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
                .data(statisticService.appStatistic()).build();
    }
}