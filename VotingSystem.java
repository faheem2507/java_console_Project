import java.util.*;

class Voter {
    private String voterId;
    private String name;
    private boolean hasVoted;
    private String votedFor;

    public Voter(String voterId, String name) {
        this.voterId = voterId;
        this.name = name;
        this.hasVoted = false;
        this.votedFor = null;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getName() {
        return name;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public String getVotedFor() {
        return votedFor;
    }

    public void castVote(String candidate) {
        if (!hasVoted) {
            this.hasVoted = true;
            this.votedFor = candidate;
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %-10s | Name: %-20s | Vote Status: %s",
                voterId, name, (hasVoted ? "✓ Voted" : "✗ Not Voted"));
    }
}

class Candidate {
    private String candidateId;
    private String name;
    private int voteCount;
    private String party;

    public Candidate(String candidateId, String name, String party) {
        this.candidateId = candidateId;
        this.name = name;
        this.party = party;
        this.voteCount = 0;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void addVote() {
        voteCount++;
    }

    public double getVotePercentage(int totalVotes) {
        if (totalVotes == 0) return 0;
        return (voteCount * 100.0) / totalVotes;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-25s | Party: %-20s | Votes: %d",
                candidateId, name, party, voteCount);
    }
}

public class VotingSystem {
    private static ArrayList<Voter> voters;
    private static ArrayList<Candidate> candidates;
    private static Scanner scanner = new Scanner(System.in);
    private static boolean votingStarted = false;
    private static boolean votingEnded = false;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      VOTING SYSTEM SIMULATION           ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        voters = new ArrayList<>();
        candidates = new ArrayList<>();

        boolean exit = false;
        while (!exit) {
            if (!votingStarted) {
                displayAdminMenu();
            } else {
                displayVotingMenu();
            }

            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            if (!votingStarted) {
                switch (choice) {
                    case "1":
                        registerVoter();
                        break;
                    case "2":
                        addCandidate();
                        break;
                    case "3":
                        displayCandidates();
                        break;
                    case "4":
                        displayVoters();
                        break;
                    case "5":
                        startVoting();
                        break;
                    case "6":
                        System.out.println("\n✓ Thank you for using Voting System. Goodbye!");
                        exit = true;
                        break;
                    default:
                        System.out.println("❌ Invalid option!\n");
                }
            } else {
                switch (choice) {
                    case "1":
                        castVote();
                        break;
                    case "2":
                        checkVoterStatus();
                        break;
                    case "3":
                        displayLiveResults();
                        break;
                    case "4":
                        endVoting();
                        votingStarted = false;
                        votingEnded = true;
                        break;
                    case "5":
                        displayFinalResults();
                        exit = true;
                        break;
                    default:
                        System.out.println("❌ Invalid option!\n");
                }
            }
        }

        scanner.close();
    }

    private static void displayAdminMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       ADMIN SETUP MENU                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Register Voter                       ║");
        System.out.println("║ 2. Add Candidate                        ║");
        System.out.println("║ 3. Display Candidates                   ║");
        System.out.println("║ 4. Display All Voters                   ║");
        System.out.println("║ 5. Start Voting                         ║");
        System.out.println("║ 6. Exit                                 ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void displayVotingMenu() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       VOTING MENU                       ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Cast Vote                            ║");
        System.out.println("║ 2. Check Voting Status                  ║");
        System.out.println("║ 3. Display Live Results                 ║");
        System.out.println("║ 4. End Voting                           ║");
        System.out.println("║ 5. View Final Results & Exit            ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void registerVoter() {
        System.out.println("\n📝 REGISTER NEW VOTER");
        System.out.println("────────────────────");

        System.out.print("Enter Voter ID: ");
        String voterId = scanner.nextLine().trim();

        // Check if voter already exists
        for (Voter v : voters) {
            if (v.getVoterId().equals(voterId)) {
                System.out.println("❌ Voter ID already registered!\n");
                return;
            }
        }

        System.out.print("Enter Voter Name: ");
        String name = scanner.nextLine().trim();

        if (voterId.isEmpty() || name.isEmpty()) {
            System.out.println("❌ Voter ID and Name are required!\n");
            return;
        }

        Voter voter = new Voter(voterId, name);
        voters.add(voter);
        System.out.println("✓ Voter registered successfully!\n");
    }

    private static void addCandidate() {
        System.out.println("\n👤 ADD NEW CANDIDATE");
        System.out.println("───────────────────");

        System.out.print("Enter Candidate ID: ");
        String candidateId = scanner.nextLine().trim();

        // Check if candidate already exists
        for (Candidate c : candidates) {
            if (c.getCandidateId().equals(candidateId)) {
                System.out.println("❌ Candidate ID already exists!\n");
                return;
            }
        }

        System.out.print("Enter Candidate Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Party Name: ");
        String party = scanner.nextLine().trim();

        if (candidateId.isEmpty() || name.isEmpty() || party.isEmpty()) {
            System.out.println("❌ All fields are required!\n");
            return;
        }

        Candidate candidate = new Candidate(candidateId, name, party);
        candidates.add(candidate);
        System.out.println("✓ Candidate added successfully!\n");
    }

    private static void displayCandidates() {
        if (candidates.isEmpty()) {
            System.out.println("\n❌ No candidates registered!\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    REGISTERED CANDIDATES                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        for (Candidate c : candidates) {
            System.out.println("║ " + c + " ║");
        }

        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    private static void displayVoters() {
        if (voters.isEmpty()) {
            System.out.println("\n❌ No voters registered!\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    REGISTERED VOTERS                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        for (Voter v : voters) {
            System.out.println("║ " + v + " ║");
        }

        System.out.println("║                                                                ║");
        System.out.println("║ Total Registered: " + voters.size() + " voters" + 
                String.format("%" + (47 - String.valueOf(voters.size()).length()) + "s", "") + "║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    private static void startVoting() {
        if (voters.isEmpty() || candidates.isEmpty()) {
            System.out.println("\n❌ Register voters and candidates before starting voting!\n");
            return;
        }

        votingStarted = true;
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        ✓ VOTING STARTED                 ║");
        System.out.println("║   Registered Voters: " + voters.size());
        System.out.println("║   Candidates: " + candidates.size());
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    private static void castVote() {
        System.out.println("\n🗳 CAST YOUR VOTE");
        System.out.println("─────────────────");

        System.out.print("Enter your Voter ID: ");
        String voterId = scanner.nextLine().trim();

        Voter voter = findVoter(voterId);
        if (voter == null) {
            System.out.println("❌ Voter not found!\n");
            return;
        }

        if (voter.hasVoted()) {
            System.out.println("❌ You have already voted! One vote per user allowed.\n");
            return;
        }

        displayCandidates();

        System.out.print("Enter Candidate ID to vote for: ");
        String candidateId = scanner.nextLine().trim();

        Candidate candidate = findCandidate(candidateId);
        if (candidate == null) {
            System.out.println("❌ Candidate not found!\n");
            return;
        }

        voter.castVote(candidate.getName());
        candidate.addVote();

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       ✓ VOTE CAST SUCCESSFULLY          ║");
        System.out.println("║ You voted for: " + String.format("%-23s", candidate.getName()) + "║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    private static void checkVoterStatus() {
        System.out.println("\n📋 CHECK VOTER STATUS");
        System.out.println("────────────────────");

        System.out.print("Enter Voter ID: ");
        String voterId = scanner.nextLine().trim();

        Voter voter = findVoter(voterId);
        if (voter == null) {
            System.out.println("❌ Voter not found!\n");
            return;
        }

        System.out.println("\n" + voter + "\n");
    }

    private static void displayLiveResults() {
        if (candidates.isEmpty()) {
            System.out.println("\n❌ No candidates registered!\n");
            return;
        }

        int totalVotes = 0;
        for (Candidate c : candidates) {
            totalVotes += c.getVoteCount();
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     LIVE VOTING RESULTS                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        if (totalVotes == 0) {
            System.out.println("║ No votes cast yet!                                             ║");
        } else {
            for (Candidate c : candidates) {
                int votes = c.getVoteCount();
                double percentage = c.getVotePercentage(totalVotes);
                String bar = createProgressBar(percentage, 20);
                System.out.printf("║ %-25s | Votes: %3d | %.1f%% %s║\n",
                        c.getName(), votes, percentage, bar);
            }
        }

        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Votes Cast: %-47s║\n", totalVotes);
        System.out.printf("║ Total Voters Registered: %-40s║\n", voters.size());
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    private static void endVoting() {
        System.out.println("\n⏹ ENDING VOTING PROCESS");
        System.out.println("───────────────────────");
        System.out.println("✓ Voting has been ended.");
        System.out.println("✓ Preparing final results...\n");
    }

    private static void displayFinalResults() {
        int totalVotes = 0;
        for (Candidate c : candidates) {
            totalVotes += c.getVoteCount();
        }

        // Sort candidates by vote count (descending)
        candidates.sort((a, b) -> Integer.compare(b.getVoteCount(), a.getVoteCount()));

        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ELECTION FINAL RESULTS                               ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");

        System.out.println("📊 VOTING STATISTICS");
        System.out.println("───────────────────");
        System.out.println("Total Registered Voters: " + voters.size());
        System.out.println("Total Votes Cast: " + totalVotes);
        System.out.println("Voter Turnout: " + String.format("%.2f%%", (totalVotes * 100.0) / voters.size()));

        System.out.println("\n🏆 FINAL STANDINGS");
        System.out.println("───────────────────");

        for (int i = 0; i < candidates.size(); i++) {
            Candidate c = candidates.get(i);
            int votes = c.getVoteCount();
            double percentage = c.getVotePercentage(totalVotes);
            String bar = createProgressBar(percentage, 25);

            System.out.println("\n" + (i + 1) + ". " + c.getName() + " (" + c.getParty() + ")");
            System.out.println("   Votes: " + votes + " | " + String.format("%.2f%%", percentage));
            System.out.println("   " + bar);
        }

        if (candidates.size() > 0) {
            System.out.println("\n👑 WINNER: " + candidates.get(0).getName() + 
                    " (" + candidates.get(0).getParty() + ")");
            System.out.println("   Total Votes: " + candidates.get(0).getVoteCount());
        }

        System.out.println("\n✓ Thank you for using Voting System Simulation!\n");
    }

    private static String createProgressBar(double percentage, int width) {
        int filled = (int) (percentage / 100 * width);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        bar.append("]");
        return bar.toString();
    }

    private static Voter findVoter(String voterId) {
        for (Voter v : voters) {
            if (v.getVoterId().equals(voterId)) {
                return v;
            }
        }
        return null;
    }

    private static Candidate findCandidate(String candidateId) {
        for (Candidate c : candidates) {
            if (c.getCandidateId().equals(candidateId)) {
                return c;
            }
        }
        return null;
    }
}
