package com.xclone.learning.Services;

import com.xclone.learning.Entity.JournalEntity;
import com.xclone.learning.Entity.User;
import com.xclone.learning.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryServices {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserServices userServices;



    @Transactional
    public void saveEntry(JournalEntity journalEntity, String userName){
        try {
            User user = userServices.findByUserName(userName);
            journalEntity.setDate(LocalDateTime.now());
            JournalEntity saved = journalEntryRepository.save(journalEntity);
            user.getJournalEntries().add(saved);
            userServices.saveUser(user);
        } catch (Exception e){
            System.out.println("Exception: "+ e);
            throw new RuntimeException("An error occurred while saving the entry. "+e);
        }
    }

    public void saveEntry(JournalEntity journalEntity){
        try {
            journalEntryRepository.save(journalEntity);
        } catch (Exception e){
            System.out.println("Exception: "+ e);
        }
    }

    public List<JournalEntity> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntity> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName){
        boolean removed = false;
        try {
            User user = userServices.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userServices.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry.",e);
        }
        return removed;
    }

//    public List<JournalEntry> findByUserName(String userName){
//
//    }
}
