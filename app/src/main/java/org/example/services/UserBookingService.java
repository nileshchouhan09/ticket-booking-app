package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private  ObjectMapper objectMapper = new ObjectMapper();

    private  static final String USERS_PATH = "../localDb/users.json";

    public  UserBookingService (User user) throws IOException {
            this.user = user;
            loadUser();

    }

    public UserBookingService() throws IOException {

        loadUser();


    }

    public List<User> loadUser() throws IOException{
        File users = new File(USERS_PATH);
       return  userList = objectMapper.readValue(users, new TypeReference<java.util.List<org.example.entities.User>>() {});
    }

    public Boolean loginUser (){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),user1.getHashPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;

        }
        catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File users = new File(USERS_PATH);
        objectMapper.writeValue(users, userList);
    }

    public void fetchBooking(){
        user.printTicket();
    }
    public Boolean cancelBooking(String ticketId){
       try{
           user.getTicketsBooked().stream().filter(e->e.getTicketId().equals(ticketId)).findFirst().ifPresent(e-> user.getTicketsBooked().remove(ticketId));
           saveUserListToFile();
           return Boolean.TRUE;
       }
       catch(IOException ex){
           return Boolean.FALSE;
       }
    }

    public List<Train> getTrain (String source, String dest) {

       try{
           TrainService trainService = new TrainService();
           return trainService.searchTrain(source,dest);
       }
       catch(Exception ex){
           return new ArrayList<>();
       }


    }

    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }
    public Boolean BookTrainSeat(Train train, int row, int seat){
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if(row>=0 && row<seats.size() && seat>=0 && seat<seats.size()){
                if(seats.get(row).get(seat)==seat){
                    seats.get(row).set(seat,1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return Boolean.TRUE;
                }
                else{
                    return false;
                }

            }else {
                return false;
            }
        }
        catch(Exception ex){
            return Boolean.FALSE;
        }
    }

    }
