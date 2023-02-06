package ru.otus.crm.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client", orphanRemoval = true)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        for (Phone phone : this.phones) {
            phone.setClient(this);
        }
    }

    @Override
    public Client clone() {
        Client clonedClient = new Client(this.id, this.name);
        clonedClient.setAddress(new Address(this.getAddress().getId(), this.getAddress().getStreet()));
        clonedClient.setPhones(this.getPhones()
                .stream()
                .map(phone -> new Phone(phone.getId(), phone.getNumber(), clonedClient))
                .toList()
        );
        return clonedClient;
    }
}
