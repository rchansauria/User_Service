package com.projects.user.service.services.impl;

import com.projects.user.service.entities.Hotel;
import com.projects.user.service.entities.Rating;
import com.projects.user.service.entities.User;
import com.projects.user.service.exceptions.ResourceNotFoundException;
import com.projects.user.service.repositories.UserRepository;
import com.projects.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public User createUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Rating[] ratings = restTemplate.getForObject("http://RATINGSERVICE/ratings/user/" + user.getUserId(), Rating[].class);
        List<Rating> ratingList = Arrays.stream(ratings).toList();
        List<Rating> ratingNewList = ratingList.stream().map(rating -> {
           ResponseEntity<Hotel> hotel =  restTemplate.getForEntity("http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);
           rating.setHotel(hotel.getBody());
           return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingNewList);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
