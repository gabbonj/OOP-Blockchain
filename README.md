# JAVA Coin Using Language Objects

## Scope
The scope of the project is to implement a simple cryptocurrency using concepts from Object-Oriented-Programming.

The project comes with a JavaFX GUI that shows how the blockchain works by direct interaction. 

The project is the final assignment for the course "Object-Oriented Programming" at the Università di Modena e Reggio Emilia - Unimore, Faculty of Computer Science and Engineering, Academic Year 2023/2024.
- Course: Object-Oriented Programming (https://github.com/nbicocchi/learn-java-core)
- Professor: Prof. Nicola Bicocchi


## Class diagram
```mermaid
classDiagram
direction BT
class Block {
+ Block(int, int, ArrayList~Event~, Wallet)
- int nonce
- ArrayList~Event~ events
- int zeros
- Wallet miner
+ verifyHash() boolean
+ clone() Block
+ verifyBlockHash(Block) boolean
- formattingEvents() String
+ toString() String
+ verifyBlock(Block) boolean
+ hashCode() int
+ verify() boolean
+ verifyBlockTransactions(Block) boolean
+ equals(Object) boolean
+ verifyTransactions() boolean
  Wallet miner
  ArrayList~Event~ events
  int nonce
  int zeros
  }
  class Blockchain {
+ Blockchain()
- int max_block_events
  ~ int next_zeros
- Date updated
- LinkedList~Block~ blocks
- ArrayList~Event~ pending
+ equals(Object) boolean
+ verify() boolean
- updateBalanceTransaction(Map~Wallet, Float~, Transaction) void
+ verifyBlockchain(Blockchain) boolean
+ hashCode() int
+ balances() Map~Wallet, Float~
+ walletBalance(Wallet) float
+ activeWallets() Set~Wallet~
- updateBalance(Map~Wallet, Float~, Wallet, Float) void
+ activeBlockchainWallets(Blockchain) Set~Wallet~
+ addBlock(Block) boolean
- reward(int) float
+ clone() Blockchain
+ addPending(Event) boolean
  ArrayList~Event~ pending
  LinkedList~Block~ blocks
  int max_block_events
  int next_zeros
  Date updated
  }
  class BlockchainView {
+ BlockchainView()
- createWallet() void
- updateWallets() void
- randomTransactions() void
+ initialize() void
- update() void
- createGenesis() void
- walletFormat(Wallet) String
- updatePending() void
- updadateBlocks() void
  Wallet randomWallet
  int eventNumber
  }
  class Core {
+ Core(Blockchain)
- Set~Wallet~ wallets
- Blockchain blockchain
+ addMinedBlock(Block) void
+ mostTrusted() Blockchain
+ addPending(Event) boolean
+ checkTrust() boolean
+ pullMostTrusted() void
+ updateWallets() void
  Blockchain blockchain
  Set~Wallet~ wallets
  }
  class Creation {
+ Creation(Wallet)
+ Creation(Date, Wallet)
- Wallet created
+ toString() String
+ hashCode() int
+ clone() Creation
+ equals(Object) boolean
  Wallet created
  }
  class Event {
+ Event()
+ Event(Date)
- Date date
+ hashCode() int
+ equals(Object) boolean
+ clone() Event
  Date date
  }
  class Main {
+ Main()
+ main(String[]) void
+ start(Stage) void
  }
  class Transaction {
+ Transaction(Wallet, Wallet, float, byte[])
+ Transaction(Date, Wallet, Wallet, float, byte[])
- Wallet from
- byte[] signture
- float amount
- Wallet to
+ equals(Object) boolean
+ verify() boolean
+ verifyTransaction(Wallet, Wallet, float, byte[]) boolean
+ hashCode() int
+ toString() String
+ clone() Transaction
  byte[] signture
  Wallet from
  float amount
  Wallet to
  }
  class Wallet {
+ Wallet()
- Blockchain personalBlockchain
- PublicKey publicKey
+ pullFromCore(Core) void
+ mineOnBlockchain() boolean
+ hashCode() int
+ toString() String
+ equals(Object) boolean
+ mine() Block
+ pullFromCore(Core, boolean) void
+ createTransaction(Wallet, float) Transaction
  PublicKey publicKey
  Blockchain personalBlockchain
  }

Block "1" *--> "miner 1" Wallet
BlockchainView  ..>  Block : «create»
BlockchainView  ..>  Blockchain : «create»
BlockchainView  ..>  Core : «create»
BlockchainView "1" *--> "core 1" Core
BlockchainView  ..>  Creation : «create»
BlockchainView "1" *--> "myWallet 1" Wallet
BlockchainView  ..>  Wallet : «create»
Core "1" *--> "blockchain 1" Blockchain
Creation  -->  Event
Creation "1" *--> "created 1" Wallet
Transaction  -->  Event
Transaction "1" *--> "to 1" Wallet
Wallet  ..>  Block : «create»
Wallet "1" *--> "personalBlockchain 1" Blockchain
Wallet  ..>  Blockchain : «create»
Wallet  ..>  Transaction : «create»
````