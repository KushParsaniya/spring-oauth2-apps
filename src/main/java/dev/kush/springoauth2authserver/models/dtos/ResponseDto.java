package dev.kush.springoauth2authserver.models.dtos;

public record ResponseDto(int status, Object data, String message) {
}
