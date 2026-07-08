package pe.joedayz.workshop.competition;

import static ch.martinelli.demo.aitaf.db.Tables.COMPETITION;
import static ch.martinelli.demo.aitaf.db.Tables.RESULT;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for competitions (CU-001). Uses jOOQ's {@link DSLContext}.
 */
@Repository
public class CompetitionRepository {

    private final DSLContext dsl;

    public CompetitionRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<CompetitionRecord> findAll() {
        return dsl.selectFrom(COMPETITION)
                .orderBy(COMPETITION.DATE.desc(), COMPETITION.NAME.asc())
                .fetch();
    }

    public Optional<CompetitionRecord> findById(Long id) {
        return dsl.selectFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .fetchOptional();
    }

    /**
     * Inserts the competition when it has no id, otherwise updates it.
     */
    public CompetitionRecord save(CompetitionRecord competition) {
        if (competition.getId() == null) {
            return dsl.insertInto(COMPETITION)
                    .set(COMPETITION.NAME, competition.getName())
                    .set(COMPETITION.DATE, competition.getDate())
                    .set(COMPETITION.LOCATION, competition.getLocation())
                    .set(COMPETITION.STATUS, competition.getStatus())
                    .returning()
                    .fetchOne();
        }
        dsl.update(COMPETITION)
                .set(COMPETITION.NAME, competition.getName())
                .set(COMPETITION.DATE, competition.getDate())
                .set(COMPETITION.LOCATION, competition.getLocation())
                .set(COMPETITION.STATUS, competition.getStatus())
                .where(COMPETITION.ID.eq(competition.getId()))
                .execute();
        return competition;
    }

    public void deleteById(Long id) {
        dsl.deleteFrom(COMPETITION)
                .where(COMPETITION.ID.eq(id))
                .execute();
    }

    /**
     * Checks the uniqueness of the competition name (RN-001), optionally
     * ignoring a given id when editing an existing competition.
     */
    public boolean nameExists(String name, Long excludeId) {
        return dsl.fetchExists(
                dsl.selectFrom(COMPETITION)
                        .where(COMPETITION.NAME.equalIgnoreCase(name))
                        .and(excludeId == null ? DSL.noCondition() : COMPETITION.ID.ne(excludeId)));
    }

    /**
     * Indicates whether the competition already has registered results
     * (used to warn before deletion, RN-005).
     */
    public boolean hasResults(Long id) {
        return dsl.fetchExists(
                dsl.selectFrom(RESULT)
                        .where(RESULT.COMPETITION_ID.eq(id)));
    }
}