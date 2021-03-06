package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.time.LocalDate;
import java.util.Date;

public class UserGetDTO {

  private Long id;
  private String name;
  private String username;
  private Boolean loggedIn;
  private Date birthDate;
  private LocalDate creationDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

    public Boolean getLoggedIn() {
        return this.loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

  public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
