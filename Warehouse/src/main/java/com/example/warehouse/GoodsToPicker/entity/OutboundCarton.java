package com.example.warehouse.GoodsToPicker.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
public class OutboundCarton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cartonNumber;
    private String status;
    private Timestamp lastUpdated;

    @OneToMany(mappedBy = "carton")
    private List<CartonItem> items;
    public OutboundCarton copy(String newCartonNumber) {
        OutboundCarton copy = new OutboundCarton();
        copy.setCartonNumber(newCartonNumber);
        copy.setStatus(this.status);  // Copy status or set a new one
        copy.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        // Copy other properties if needed
        return copy;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private String getStatus() {
        return status;
    }
    public void setStatus(String inProgress) {
    }

    public void setLastUpdated(Timestamp timestamp) {
    }

    private void setCartonNumber(String newCartonNumber) {
    }
}
