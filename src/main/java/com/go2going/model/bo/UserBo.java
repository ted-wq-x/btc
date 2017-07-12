package com.go2going.model.bo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by wangq on 2017/7/12.
 */
@Component
public class UserBo {
  @Value("${login.username}")
  private String name;

  @Value("${login.password}")
  private String password;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "UserBo{" +
            "name='" + name + '\'' +
            ", password='" + password + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserBo userBo = (UserBo) o;

    if (name != null ? !name.equals(userBo.name) : userBo.name != null) return false;
    return password != null ? password.equals(userBo.password) : userBo.password == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }
}
