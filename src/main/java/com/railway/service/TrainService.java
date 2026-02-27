package com.railway.service;

import com.railway.dto.TrainRequest;
import com.railway.dto.TrainResponse;
import com.railway.model.Train;
import com.railway.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {
    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public List<TrainResponse> getAllTrains() {
        return trainRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<TrainResponse> searchTrains(String source, String destination) {
        return trainRepository.searchTrains(source, destination).stream().map(this::toResponse).toList();
    }

    public TrainResponse getTrain(Long id) {
        return toResponse(getTrainEntity(id));
    }

    public TrainResponse createTrain(TrainRequest request) {
        Train train = new Train();
        applyRequest(train, request);
        train.setSeatsAvailable(request.totalSeats());
        return toResponse(trainRepository.save(train));
    }

    public TrainResponse updateTrain(Long id, TrainRequest request) {
        Train train = getTrainEntity(id);
        applyRequest(train, request);
        if (train.getSeatsAvailable() > train.getTotalSeats()) {
            train.setSeatsAvailable(train.getTotalSeats());
        }
        return toResponse(trainRepository.save(train));
    }

    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    public Train getTrainEntity(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Train not found"));
    }

    private void applyRequest(Train train, TrainRequest request) {
        train.setTrainName(request.trainName());
        train.setSource(request.source());
        train.setDestination(request.destination());
        train.setDepartureTime(request.departureTime());
        train.setArrivalTime(request.arrivalTime());
        train.setPrice(request.price());
        train.setTotalSeats(request.totalSeats());
    }

    private TrainResponse toResponse(Train train) {
        return new TrainResponse(
                train.getTrainNo(),
                train.getTrainName(),
                train.getSource(),
                train.getDestination(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                train.getPrice(),
                train.getTotalSeats(),
                train.getSeatsAvailable()
        );
    }
}
