package com.app.msc_project_administrator.user;


import com.app.msc_project_administrator.project.Project;
import com.app.msc_project_administrator.project.Tag;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  private String firstname;
  private String lastname;
  private String email;
  private String password;

  @ManyToMany
  @JoinTable(
          name = "supervisor_tags",
          joinColumns = @JoinColumn(name = "supervisor_id"),
          inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> expertiseTags;  // Tags representing supervisor's expertise

  @OneToMany(mappedBy = "supervisor")
  private Set<Project> projects;  // Projects supervised by the user

  @Enumerated(EnumType.STRING)
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
