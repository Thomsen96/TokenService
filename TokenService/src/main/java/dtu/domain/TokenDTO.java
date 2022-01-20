package dtu.domain;

import java.util.List;

public class TokenDTO {
  List<String> tokens;

  public TokenDTO(List<String> tokens) {
    this.tokens = tokens;
  }

  public TokenDTO() {
  }

  public List<String> getTokens() {
    return tokens;
  }

  public void setTokens(List<String> tokens) {
    this.tokens = tokens;
  }

  @Override
  public String toString() {
    return "TokenDTO [tokens=" + tokens + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TokenDTO other = (TokenDTO) obj;
    if (tokens == null) {
      if (other.tokens != null)
        return false;
    } else if (!tokens.equals(other.tokens))
      return false;
    return true;
  }
  
  
}
