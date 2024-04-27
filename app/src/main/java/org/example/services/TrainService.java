package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Train;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {


    private ObjectMapper objectMapper = new ObjectMapper();

    List<Train> trainList;

    public static final String TRAIN_DB_PATH = "src/main/java/org.example/localDB/trains.json";




    public List<Train> searchTrain(String source, String dest) {
        return trainList.stream().filter(train -> validTrain(train, source, dest)).collect(Collectors.toList());

    }

    public boolean validTrain(Train train, String source, String dest) {
        List<String> stationOrder = train.getStations();
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destIndex = stationOrder.indexOf(dest.toLowerCase());

        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }
    public void addTrain(Train newTrain){
        Optional<Train> existingTrains = trainList.stream().filter(train->train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();

        if(existingTrains.isPresent()){
            updateTrain(newTrain);
        }
        else {
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train newTrain){
        OptionalInt index = IntStream.range(0,trainList.size()).filter(i->trainList.get(i).getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();
        if(index.isPresent()){
            trainList.set(index.getAsInt(), newTrain);
            saveTrainListToFile();
        }
        else{
            trainList.add(newTrain);
        }
    }
    public void saveTrainListToFile(){
        try{
            objectMapper.writeValue(new File(TRAIN_DB_PATH),trainList);
        }catch(Exception e){
            e.printStackTrace();

        }
    }

}