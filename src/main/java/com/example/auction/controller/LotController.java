package com.example.auction.controller;

import com.example.auction.dto.*;
import com.example.auction.exceptions.*;
import com.example.auction.model.LotStatus;
import com.example.auction.service.LotService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/lot")
//@Api(description = "Контроллер для работы с лотами на аукционе")
public class LotController {

    private final LotService lotService;

    //private static final Logger logger = new Logger(LotController.class.getName());


    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    //
    @GetMapping("/{id}/first")
    //@ApiOperation("Получить информацию о первом, ставившем на лот")
    public ResponseEntity<BidDTO> getFirstBidder(@PathVariable(name = "id") Long id) {
        BidDTO dto = lotService.getFirstBidder(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dto);
    }

    // Возвращает имя ставившего на данный лот наибольшее количество раз
    // и дату его последней ставки
    @GetMapping("/{id}/frequent")
    public ResponseEntity<BidDTO> getMostFrequentBidder(@PathVariable(name = "id") Long id) {
        BidDTO dto = lotService.getMostFrequentBidder(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dto);
    }

    // Получить полную информацию о лоте
    @GetMapping("/{id}")
    public ResponseEntity<FullLotDTO> getLotInfo(@PathVariable(name = "id") Long id) {
        FullLotDTO dto = lotService.getLotInfo(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dto);
    }

    // Начать торги по лоту
    @PostMapping("/{id}/start")
    public ResponseEntity<String> startBidding(@PathVariable(name = "id") Long id) {
        try {
            lotService.startBidding(id);
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        }
        return ResponseEntity.ok("Лот переведен в статус \"начато\"");
    }

    // Сделать ставку по лоту
    @PostMapping("/{id}/bid")
    public ResponseEntity<String> makeBid(@PathVariable(name = "id") Long id,
                                          @RequestBody BidderNameDTO bidderNameDTO) {
        try {
            lotService.makeBid(id, bidderNameDTO);
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        } catch (WrongLotStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Лот в неверном статусе");
        }
        return ResponseEntity.ok("Лот переведен в статус \"начато\"");
    }

    // Остановить торги по лоту
    @PostMapping("/{id}/stop")
    public ResponseEntity<String> stopBidding(@PathVariable(name = "id") Long id) {
        try {
            lotService.stopBidding(id);
        } catch (LotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Лот не найден");
        }
        return ResponseEntity.ok("Лот переведен в статус \"остановлен\"");
    }

    // Создает новый лот
    @PostMapping("")
    public ResponseEntity<LotDTO> createLot(@RequestBody CreateLotDTO createLotDTO) {
        LotDTO dto = lotService.createLot(createLotDTO);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(dto);
    }

    // Получить все лоты, основываясь на фильтре статуса и номере страницы
    @GetMapping("")
    public ResponseEntity<List<LotDTO>> getAllLots(@RequestParam(name = "status")LotStatus lotStatus,
                                                   @RequestParam(name = "page", required = false) Integer page) {
        if (page == null) page = 0;
        List<LotDTO> dtos = lotService.getLotsPage(lotStatus, page);
        if (dtos == null || dtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        else return ResponseEntity.ok(dtos);
    }

    // Экспортировать все лоты в файл CSV
    // Экспортировать все лоты в формате id,title,status,lastBidder,currentPrice
    // в одном файле CSV
    @GetMapping("/export")
    public void exportLotsToCSV(@RequestParam(name = "page",
            required = false) HttpServletResponse response) throws IOException {
        List<FullLotDTO> fullLotDTOs = lotService.getAllFullLots();
        if (fullLotDTOs == null) return;

        response.setContentType("text/html;charset=UTF-8");
        Appendable appendable =  response.getWriter();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .build();
        try (final CSVPrinter printer = new CSVPrinter(appendable, csvFormat)) {
            fullLotDTOs.forEach(dto -> {
                try {
                    printer.printRecord(dto.getId(), dto.getTitle(),
                            dto.getStatus().getString(),
                            dto.getLastBid() == null ? "" : dto.getLastBid().getBidderName(),
                            dto.getCurrentPrice());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
