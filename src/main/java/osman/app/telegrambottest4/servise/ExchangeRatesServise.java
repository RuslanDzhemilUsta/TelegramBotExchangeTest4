package osman.app.telegrambottest4.servise;

import osman.app.telegrambottest4.exception.ServiceException;

import javax.sql.rowset.serial.SerialException;

public interface ExchangeRatesServise {

    String getUSDExchangeRate() throws ServiceException;

    String getEURExchangeRate() throws ServiceException;

    String getLIRAExchangeRate() throws ServiceException;

    String getUAHExchangeRate() throws ServiceException;

    String getUZSExchangeRate() throws ServiceException;
}
