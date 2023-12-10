package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {

}
