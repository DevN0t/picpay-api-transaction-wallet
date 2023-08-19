package com.picpay.services;

import com.picpay.domain.user.User;
import com.picpay.domain.user.UserType;
import com.picpay.dtos.UserDTO;
import com.picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount)throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Lojistas podem apenas receber transações");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception{
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário Não Encontrado"));
    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }
    public void saveUser(User user){
        this.repository.save(user);
    }
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = this.repository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
