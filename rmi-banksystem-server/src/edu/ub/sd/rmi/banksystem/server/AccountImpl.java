package edu.ub.sd.rmi.banksystem.server;

import edu.ub.sd.rmi.banksystem.interfaces.Account;
import edu.ub.sd.rmi.banksystem.interfaces.BankManager;
import edu.ub.sd.rmi.banksystem.interfaces.Client;
import edu.ub.sd.rmi.banksystem.interfaces.exceptions.NoCashAvailableException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AccountImpl extends UnicastRemoteObject implements Account {

  private BankManager bankManager;
  private Client client;
  private long        balance;
  private String      accountNumber;

  // public constructor
  public AccountImpl ( 
      BankManager bankManager, 
      Client client, 
      String accountNumber) 
	  throws RemoteException {
    super();
    this.bankManager = bankManager;
    this.client      = client;
    this.balance     = 0;
    this.accountNumber = accountNumber;
  }

  public void deposit(long amount) {
    balance += amount;
  }

  public BankManager getBankManager() 
      throws RemoteException {
    return bankManager;
  }

  public Client getClient() 
      throws RemoteException {
    return client;
  }

  public long getBalance() 
      throws RemoteException {
    return balance;
  }

  public long getCash(long amount)
      throws NoCashAvailableException, RemoteException {
    if (amount > balance) {
      throw new NoCashAvailableException();
    }
    balance = balance - amount;
    return amount;
  }
}   
