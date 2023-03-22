package com.example.auction.service;

import com.example.auction.dto.*;
import com.example.auction.exceptions.*;
import com.example.auction.model.*;
import com.example.auction.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LotServiceImpl implements LotService {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    private final BidderRepository bidderRepository;

    public LotServiceImpl(LotRepository lotRepository, BidRepository bidRepository,
                          BidderRepository bidderRepository) {
        this.lotRepository = lotRepository;
        this.bidRepository = bidRepository;
        this.bidderRepository = bidderRepository;
    }

    // Получить информацию о первом ставившем на лот
    @Override
    public BidDTO getFirstBidder(Long lotID) {
        List<Bid> bids = bidRepository.findFirstBid(lotID);
        if (bids.isEmpty()) return null;
        return BidDTO.fromBid(bids.get(0));
    }

    // Возвращает имя ставившего на данный лот наибольшее количество раз
    // Наибольшее количество вычисляется из общего количества ставок на лот
    // Результат: имя ставившего и дата его последней ставки
    @Override
    public BidDTO getMostFrequentBidder(Long lotID) {
        List<Long> bidderIDs = bidRepository.findMostFrequentBidderId(lotID);
        if (bidderIDs.isEmpty()) return null;
        Long mostFrequentBidderID = bidderIDs.get(0);
        List<Bid> bids = bidRepository
                .findByBidderIdAndLotIdOrderByBidDateTimeDesc(mostFrequentBidderID, lotID);

        return BidDTO.fromBid(bids.get(0));
    }

    // Получить полную информацию о лоте
    // Возвращает полную информацию о лоте с последним ставившим и текущей ценой
    @Override
    public FullLotDTO getLotInfo(Long lotID) {
        Lot lot = lotRepository.findById(lotID).orElse(null);
        if (lot == null) return null;
        FullLotDTO dto = FullLotDTO.fromLot(lot);
        dto.setCurrentPrice(getCurrentLotPrice(lotID));
        List<Bid> bidsOnLot = bidRepository.findAllByLotIdOrderByBidDateTimeAsc(lotID);
        if (!bidsOnLot.isEmpty()) {
            dto.setLastBid(BidDTO.fromBid(bidsOnLot.get(bidsOnLot.size() - 1)));
        }
        return dto;
    }

    // Начать торги по лоту
    // Переводит лот в состояние "начато", которое позволяет делать ставки на лот.
    @Override
    public void startBidding(Long lotID) throws LotNotFoundException {
        Lot lot = lotRepository.findById(lotID).orElse(null);
        if (lot == null) {
            throw new LotNotFoundException();
        }
        if (lot.getStatus().equals(LotStatus.STARTED.getString())) return;
        lot.setStatus(LotStatus.STARTED.getString());
        lotRepository.save(lot);
    }

    // Сделать ставку по лоту
    // Создает новую ставку по лоту. Если лот в статусе CREATED или STOPPED,
    // то должна вернуться ошибка
    @Override
    public void makeBid(Long lotID, BidderNameDTO bidderNameDTO)
            throws LotNotFoundException, WrongLotStatusException {
        Lot lot = lotRepository.findById(lotID).orElse(null);
        if (lot == null) {
            throw new LotNotFoundException();
        }
        if (lot.getStatus().equals(LotStatus.CREATED.getString()) ||
            lot.getStatus().equals(LotStatus.STOPPED.getString())) {
            throw new WrongLotStatusException();
        }
        Bidder bidder = bidderRepository.findByName(bidderNameDTO.getName()).orElse(null);
        if (bidder == null) {
            bidder = new Bidder();
            bidder.setId(null);
            bidder.setName(bidderNameDTO.getName());
            bidderRepository.save(bidder);
        }
        Bid bid = new Bid();
        bid.setId(null);
        bid.setBidder(bidder);
        bid.setBidDateTime(LocalDateTime.now());
        bid.setLot(lot);
        bidRepository.save(bid);
    }

    // Остановить торги по лоту
    // Переводит лот в состояние "остановлен", которое запрещает делать ставки на лот.
    // Если лот уже находится в состоянии "остановлен", то ничего не делает.
    @Override
    public void stopBidding(Long lotID) throws LotNotFoundException {
        Lot lot = lotRepository.findById(lotID).orElse(null);
        if (lot == null) {
            throw new LotNotFoundException();
        }
        if (lot.getStatus() == LotStatus.STOPPED.getString()) return;
        lot.setStatus(LotStatus.STOPPED.getString());
        lotRepository.save(lot);
    }

    // Создает новый лот
    // Метод создания нового лота, если есть ошибки
    // в полях объекта лота - то нужно вернуть статус с ошибкой
    @Override
    public LotDTO createLot(CreateLotDTO createLotDTO) {
        Lot lot = LotDTO.fromCreateLotDTO(createLotDTO).toLot();
        if (lot.getDescription().isEmpty() || lot.getTitle().isEmpty()) {
            return null;
        }
        if (lot.getStartPrice() <=0 || lot.getBidPrice() <= 0) {
            return null;
        }
        lot.setId(null);
        lotRepository.save(lot);
        return LotDTO.fromLot(lot);
    }

    // Получить все лоты, основываясь на фильтре статуса и номере страницы
    // Возвращает все записи о лотах без информации о текущей цене и победителе
    // постранично. Если страница не указана, то возвращается первая страница.
    // Номера страниц начинаются с 0. Лимит на количество лотов на странице - 10 штук.
    @Override
    public List<LotDTO> getLotsPage(LotStatus lotStatus, Integer page) {
        final int LOTS_PER_PAGE = 10;

        PageRequest pageRequest = PageRequest.of(page, LOTS_PER_PAGE);
        List<Lot> lots = lotRepository.findAllByStatus(lotStatus.getString(), pageRequest);
        if (lots.isEmpty()) return null;
        List<LotDTO> dtos = new ArrayList<>();
        for (Lot lot : lots) {
            LotDTO dto = LotDTO.fromLot(lot);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public Integer getCurrentLotPrice(Long lotID) {
        Lot lot = lotRepository.findById(lotID).orElse(null);
        if (lot == null) return null;
        Integer numberOfBids = bidRepository.countAllByLotId(lotID);
        return lot.getStartPrice() + lot.getBidPrice() * numberOfBids;
    }

    @Override
    public List<LotDTO> getAllLots() {
        List<Lot> lots = lotRepository.findAll();
        if (lots.isEmpty()) return null;
        List<LotDTO> dtos = new ArrayList<>();
        for (Lot lot : lots) {
            LotDTO dto = LotDTO.fromLot(lot);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<FullLotDTO> getAllFullLots() {
        List<Lot> lots = lotRepository.findAll();
        if (lots.isEmpty()) return null;
        List<FullLotDTO> dtos = new ArrayList<>();
        for (Lot lot : lots) {
            FullLotDTO dto = getLotInfo(lot.getId());
            dtos.add(dto);
        }
        return dtos;
    }

}
