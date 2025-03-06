package ru.practicum.shareit.item.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "items")
@FieldDefaults (level = AccessLevel.PRIVATE)
public class Item {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "item_id")
    Long id;

    String name;


    @Column (length = 250, name = "description")
    String description;

    Boolean available;

    @JoinColumn (name = "user_id")
    @ManyToOne
    User owner;


    @ToString.Exclude
    @ElementCollection (fetch = FetchType.LAZY)
    @Column (name = "text")
    @CollectionTable (name = "comments", joinColumns = @JoinColumn (name = "item_id"))
    List<String> comments;
}
