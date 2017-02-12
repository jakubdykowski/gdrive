package com.googlecode.gmail4j.javamail;

public enum ImapGmailLabel
{
  ALL_MAIL("[Gmail]/All Mail"),

  DRAFTS("[Gmail]/Drafts"),

  SENT_MAIL("[Gmail]/Sent Mail"),

  SPAM("[Gmail]/Spam"),

  STARRED("[Gmail]/Starred"),

  INBOX("INBOX"),

  TRASH("[Gmail]/Trash"),

  IMPORTANT("[Gmail]/Important");

  private String name;

  private ImapGmailLabel(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return this.name;
  }

  public String toString()
  {
    return this.name;
  }
}