package com.uni.libraryproj.model;

import java.util.Date;

public class Loan {
    private Integer id;
    private Integer book_id;

    private Integer member_id;

    private Date loanDate;

    private Date returnDate;

    public Loan() {
    }

    public Loan(Integer member_id, Integer book_id, Date loanDate, Date returnDate) {
        this.member_id = member_id;
        this.book_id = book_id;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Integer getId() {
        return id;
    }

    public Loan setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public Loan setMember_id(Integer member_id) {
        this.member_id = member_id;
        return this;
    }

    public Integer getBook_id() {
        return book_id;
    }

    public Loan setBook_id(Integer book_id) {
        this.book_id = book_id;
        return this;
    }

    public Integer getLoaner_id() {
        return member_id;
    }

    public Loan setLoaner_id(Integer member_id) {
        this.member_id = member_id;
        return this;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public Loan setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
        return this;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public Loan setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
        return this;
    }
}
