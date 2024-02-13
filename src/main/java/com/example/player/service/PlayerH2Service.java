/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.player.service;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.player.model.Player;
import com.example.player.model.PlayerRowMapper;
import com.example.player.repository.PlayerRepository;

import java.util.*;

@Service
public class PlayerH2Service implements PlayerRepository {

    @Autowired
    private JdbcTemplate db;

    int uniqueId = 12;

    @Override
    public ArrayList<Player> getPlayers() {
        Collection<Player> playerCollection = db.query("select * from team", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerCollection);

        return players;
    }

    @Override
    public Player getPlayerById(int playerId) {

        try {
            Player player = db.queryForObject("select * from team where playerid = ?", new PlayerRowMapper(), playerId);
            return player;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Player addPlayer(Player player) {

        db.update("insert into team(playerName, jerseyNumber,role) values (?, ?, ?)", player.getPlayerName(),
                player.getJerseyNumber(), player.getRole());

        Player savedPlayer = db.queryForObject(
                "select * from team where playerName = ? and jerseyNumber = ? and role=?",
                new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());

        return savedPlayer;
    }

    @Override
    public Player updatePlayer(int playerId, Player player) {

        if (player.getPlayerName() != null) {
            db.update("update team set playerName = ? where playerid = ?", player.getPlayerName(), playerId);
        }

        if (player.getJerseyNumber() != 0) {
            db.update("update team set jerseyNumber = ? where playerid = ?", player.getJerseyNumber(), playerId);
        }

        if (player.getRole() != null) {
            db.update("update team set role = ? where playerid = ?", player.getRole(), playerId);
        }

        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) {

        db.update("delete from team where playerid = ?", playerId);
    }

}