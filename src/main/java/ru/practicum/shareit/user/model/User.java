package ru.practicum.shareit.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;


@Entity
@Getter
@Setter
@Builder (toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE)
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Электронная почта не может быть пустой")
    @Email (message = "Электронная почта должна соответствовать шаблону name@domain.xx")
    private String email;

    @NotBlank (message = "Имя не может быть пустым")
    private String name;

    @OneToMany (mappedBy = "booker")
    private Collection<Booking> booking;

    @OneToOne (mappedBy = "owner", optional = false)
    private Item item;

}
