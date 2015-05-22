From the [Trinity DBC structure file](https://github.com/TrinityCore/TrinityCore/blob/6.x/src/server/game/DataStores/DBCStructure.h)

    struct AchievementEntry
    {
        uint32      ID;                                         // 0
        int32       Faction;                                    // 1 -1=all, 0=horde, 1=alliance
        int32       MapID;                                      // 2 -1=none
        //uint32    Supercedes;                                 // 3 its Achievement parent (can`t start while parent uncomplete, use its Criteria if don`t have own, use its progress on begin)
        char*       Title_lang;                                 // 4
        //char*     Description_lang;                           // 5
        uint32      Category;                                   // 6
        uint32      Points;                                     // 7 reward points
        //uint32    UIOrder;                                    // 8
        uint32      Flags;                                      // 9
        //uint32    IconID;                                     // 10 icon (from SpellIcon.dbc)
        //char*     Reward_lang;                                // 11
        uint32      MinimumCriteria;                            // 12 - need this count of completed criterias (own or referenced achievement criterias)
        uint32      SharesCriteria;                             // 13 - referenced achievement (counting of all completed criterias)
        uint32      CriteriaTree;                               // 14
    };
    
    struct AchievementCategoryEntry
    {
        uint32      ID;                                         // 0
        uint32      Parent;                                     // 1 -1 for main category
        //char*     Name_lang;                                  // 2
        //uint32    UIOrder;                                    // 3
    };
    
    // Temporary define until max depth is found somewhere (adt?)
    #define MAX_MAP_DEPTH -5000
    
    struct AreaTableEntry
    {
        uint32      ID;                                         // 0
        uint32      MapID;                                      // 1
        uint32      ParentAreaID;                               // 2 if 0 then it's zone, else it's zone id of this area
        uint32      AreaBit;                                    // 3, main index
        uint32      Flags[2];                                   // 4-5,
        //uint32    SoundProviderPref;                          // 6,
        //uint32    SoundProviderPrefUnderwater;                // 7,
        //uint32    AmbienceID;                                 // 8,
        //uint32    ZoneMusic;                                  // 9,
        char*       ZoneName;                                   // 10
        //uint32    IntroSound;                                 // 11
        uint32      ExplorationLevel;                           // 12
        //char*     AreaName_lang                               // 13
        uint32      FactionGroupMask;                           // 14
        uint32      LiquidTypeID[4];                            // 15-18
        //float     AmbientMultiplier;                          // 19
        //uint32    MountFlags;                                 // 20
        //uint32    UWIntroMusic;                               // 21
        //uint32    UWZoneMusic;                                // 22
        //uint32    UWAmbience;                                 // 23
        //uint32    WorldPvPID;                                 // 24 World_PVP_Area.dbc
        //uint32    PvPCombastWorldStateID;                     // 25
        //uint32    WildBattlePetLevelMin;                      // 26
        //uint32    WildBattlePetLevelMax;                      // 27
        //uint32    WindSettingsID;                             // 28
    };
    
    struct AreaTriggerEntry
    {
        uint32          ID;                                     // 0
        uint32          MapID;                                  // 1
        DBCPosition3D   Pos;                                    // 2-4
        //uint32        PhaseUseFlags                           // 5
        //uint32        PhaseID                                 // 6
        //uint32        PhaseGroupID                            // 7
        float           Radius;                                 // 8
        float           BoxLength;                              // 9
        float           BoxWidth;                               // 10
        float           BoxHeight;                              // 11
        float           BoxYaw;                                 // 12
        //uint32        ShapeType                               // 13
        //uint32        ShapeID                                 // 14
        //uint32        AreaTriggerActionSetID                  // 15
        //uint32        Flags                                   // 16
    };
    
    struct ArmorLocationEntry
    {
        uint32      ID;                                         // 0
        float       Modifier[5];                                // 1-5 multiplier for armor types (cloth...plate, no armor?)
    };
    
    struct AuctionHouseEntry
    {
        uint32      ID;                                         // 0
        uint32      FactionID;                                  // 1 id of faction.dbc for player factions associated with city
        uint32      DepositRate;                                // 2 1/3 from real
        uint32      ConsignmentRate;                            // 3
        //char*     Name_lang;                                  // 4
    };
    
    struct BankBagSlotPricesEntry
    {
        uint32      ID;                                         // 0
        uint32      Cost;                                       // 1
    };
    
    struct BannedAddOnsEntry
    {
        uint32      ID;                                         // 0
        //uint32    NameMD5[4];                                 // 1
        //uint32    VersionMD5[4];                              // 2
        //uint32    LastModified;                               // 3
        //uint32    Flags;                                      // 4
    };
    
    struct BarberShopStyleEntry
    {
        uint32      ID;                                         // 0
        uint32      Type;                                       // 1 value 0 -> hair, value 2 -> facialhair
        //char*     DisplayName_lang;                           // 2
        //char*     Description_lang                            // 3
        //float     CostModifier;                               // 4
        uint32      Race;                                       // 5
        uint32      Sex;                                        // 6
        uint32      Data;                                       // 7 (real ID to hair/facial hair)
    };
    
    struct BattlemasterListEntry
    {
        uint32      ID;                                         // 0
        int32       MapID[16];                                  // 1-16 mapid
        uint32      InstanceType;                               // 17 map type (3 - BG, 4 - arena)
        //uint32    GroupsAllowed;                              // 18 (0 or 1)
        char*       Name_lang;                                  // 19
        uint32      MaxGroupSize;                               // 20 maxGroupSize, used for checking if queue as group
        uint32      HolidayWorldState;                          // 21 new 3.1
        uint32      MinLevel;                                   // 22, min level (sync with PvPDifficulty.dbc content)
        uint32      MaxLevel;                                   // 23, max level (sync with PvPDifficulty.dbc content)
        //uint32    RatedPlayers;                               // 24 4.0.1
        //uint32    MinPlayers;                                 // 25 - 4.0.6.13596
        //uint32    MaxPlayers;                                 // 26 4.0.1
        //uint32    Flags;                                      // 27 4.0.3, value 2 for Rated Battlegrounds
        //uint32    IconFileDataID;                             // 28
        //char*     GameType_lang;                              // 29
        //uint32    Unk1;                                       // 30
    };
    
    #define MAX_OUTFIT_ITEMS 24
    
    struct CharStartOutfitEntry
    {
        //uint32    ID;                                         // 0
        uint8       RaceID;                                     // 1
        uint8       ClassID;                                    // 2
        uint8       GenderID;                                   // 3
        //uint8     OutfitID;                                   // 4
        int32       ItemID[MAX_OUTFIT_ITEMS];                   // 5-28
        uint32      PetDisplayID;                               // 29 Pet Model ID for starting pet
        uint32      PetFamilyID;                                // 30 Pet Family Entry for starting pet
    };
    
    struct CharTitlesEntry
    {
        uint32      ID;                                         // 0, title ids, for example in Quest::GetCharTitleId()
        //uint32    ConditionID;                                // 1
        char*       NameMale_lang;                              // 2 m_name_lang
        char*       NameFemale_lang;                            // 3 m_name1_lang
        uint32      MaskID;                                     // 4 m_mask_ID used in PLAYER_CHOSEN_TITLE and 1<<index in PLAYER__FIELD_KNOWN_TITLES
        //uint32    Flags;                                      // 5
    };
    
    struct ChatChannelsEntry
    {
        uint32      ID;                                         // 0
        uint32      Flags;                                      // 1
        //uint32    FactionGroup                                // 2
        char*     Name_lang;                                    // 3
        //char*     Shortcut_lang;                              // 4
    };
    
    struct ChrClassesEntry
    {
        uint32      ID;                                         // 0
        uint32      PowerType;                                  // 1
        //char*     PetNameToken                                // 2
        char*       Name_lang;                                  // 3
        //char*     NameFemale_lang;                            // 4
        //char*     NameMale_lang;                              // 5
        //char*     Filename;                                   // 6
        uint32      SpellClassSet;                              // 7
        //uint32    Flags;                                      // 8
        uint32      CinematicSequenceID;                        // 9
        uint32      AttackPowerPerStrength;                     // 10 Attack Power bonus per point of strength
        uint32      AttackPowerPerAgility;                      // 11 Attack Power bonus per point of agility
        uint32      RangedAttackPowerPerAgility;                // 12 Ranged Attack Power bonus per point of agility
        //uint32    DefaultSpec;                                // 13
        //uint32    CreateScreenFileDataID;                     // 14
        //uint32    SelectScreenFileDataID;                     // 15
        //uint32    LowResScreenFileDataID;                     // 16
        //uint32    IconFileDataID;                             // 17
        //uint32    Unk1;                                       // 18
    };
    
    struct ChrRacesEntry
    {
        uint32      ID;                                         // 0
        uint32      Flags;                                      // 1
        uint32      FactionID;                                  // 2 faction template id
        //uint32    ExplorationSoundID;                         // 3
        uint32      MaleDisplayID;                              // 4
        uint32      FemaleDisplayID;                            // 5
        //char*     ClientPrefix;                               // 6
        //uint32    BaseLanguage;                               // 7
        //uint32    CreatureType;                               // 8
        //uint32    ResSicknessSpellID;                         // 9
        //uint32    SplashSoundID;                              // 10
        //char*     ClientFileString;                           // 11
        uint32      CinematicSequenceID;                        // 12
        uint32      TeamID;                                     // 13 m_alliance (0 alliance, 1 horde, 2 neutral)
        char*       Name_lang;                                  // 14
        //char*     NameFemale_lang;                            // 15
        //char*     NameMale_lang;                              // 16
        //char*     FacialHairCustomization[2];                 // 17-18
        //char*     HairCustomization;                          // 19
        //uint32    RaceRelated;                                // 20
        //uint32    UnalteredVisualRaceID;                      // 21
        //uint32    UAMaleCreatureSoundDataID;                  // 22
        //uint32    UAFemaleCreatureSoundDataID;                // 23
        //uint32    CharComponentTextureLayoutID;               // 24
        //uint32    DefaultClassID;                             // 25
        //uint32    CreateScreenFileDataID;                     // 26
        //uint32    SelectScreenFileDataID;                     // 27
        //float     MaleCustomizeOffset[3];                     // 28-30
        //float     FemaleCustomizeOffset[3];                   // 31-33
        //uint32    NeutralRaceID;                              // 34
        //uint32    LowResScreenFileDataID;                     // 35
        //uint32    HighResMaleDisplayID;                       // 36
        //uint32    HighResFemaleDisplayID;                     // 37
        //uint32    CharComponentTexLayoutHiResID;              // 38
        //uint32    Unk;                                        // 39
    };
    
    struct ChrPowerTypesEntry
    {
        uint32      ID;                                         // 0
        uint32      ClassID;                                    // 1
        uint32      PowerType;                                  // 2
    };
    
    #define MAX_MASTERY_SPELLS 2
    
    struct ChrSpecializationEntry
    {
        uint32      ID;                                         // 0 Specialization ID
        //char*     BackgroundFile;                             // 1
        uint32      ClassID;                                    // 2
        uint32      MasterySpellID[MAX_MASTERY_SPELLS];         // 3
        uint32      OrderIndex;                                 // 4
        uint32      PetTalentType;                              // 5
        uint32      Role;                                       // 6 (0 - Tank, 1 - Healer, 2 - DPS)
        uint32      SpellIconID;                                // 7
        uint32      RaidBuffs;                                  // 8
        uint32      Flags;                                      // 9
        //char*     Name_lang;                                  // 10
        //char*     Name2_lang;                                 // 11 Same as name_lang?
        //char*     Description_lang;                           // 12
        uint32      PrimaryStatOrder[2];                        // 13-14
    };
    
    struct CinematicSequencesEntry
    {
        uint32      ID;                                         // 0
        //uint32    SoundID;                                    // 1
        //uint32    Camera[8];                                  // 2-9
    };
    
    struct CreatureDisplayInfoEntry
    {
        uint32      ID;                                         // 0
        uint32      ModelID;                                    // 1
        //uint32    SoundID;                                    // 2
        uint32      ExtendedDisplayInfoID;                      // 3
        float       CreatureModelScale;                         // 4
        //uint32    CreatureModelAlpha;                         // 5
        //char*     TextureVariation[3];                        // 6-8
        //char*     PortraitTextureName;                        // 9
        //uint32    PortraitCreatureDisplayInfoID;              // 10
        //uint32    SizeClass;                                  // 11
        //uint32    BloodID;                                    // 12
        //uint32    NPCSoundID;                                 // 13
        //uint32    ParticleColorID;                            // 14
        //uint32    CreatureGeosetData;                         // 15
        //uint32    ObjectEffectPackageID;                      // 16
        //uint32    AnimReplacementSetID;                       // 17
        //uint32    Flags;                                      // 18
        int32       Gender;                                     // 19
        //uint32    StateSpellVisualKitID;                      // 20
    };
    
    struct CreatureDisplayInfoExtraEntry
    {
        //uint32    ID;                                         // 0
        uint32      DisplayRaceID;                              // 1
        //uint32    DisplaySexID;                               // 2
        //uint32    SkinID;                                     // 3
        //uint32    FaceID;                                     // 4
        //uint32    HairStyleID;                                // 5
        //uint32    HairColorID;                                // 6
        //uint32    FacialHairID;                               // 7
        //uint32    NPCItemDisplay[11];                         // 8-18
        //uint32    Flags;                                      // 19
        //uint32    FileDataID;                                 // 20
        //uint32    Unk;                                        // 21
    };
    
    struct CreatureFamilyEntry
    {
        uint32      ID;                                         // 0
        float       MinScale;                                   // 1
        uint32      MinScaleLevel;                              // 2
        float       MaxScale;                                   // 3
        uint32      MaxScaleLevel;                              // 4
        uint32      SkillLine[2];                               // 5-6
        uint32      PetFoodMask;                                // 7
        uint32      PetTalentType;                              // 8
        //uint32    CategoryEnumID;                             // 9
        char*       Name_lang;                                  // 10
        //char*     IconFile;                                   // 11
    };
    
    struct CreatureModelDataEntry
    {
        uint32      ID;                                         // 0
        uint32      Flags;                                      // 1
        //uint32    FileDataID;                                 // 2
        //uint32    SizeClass;                                  // 3
        //float     ModelScale;                                 // 4
        //uint32    BloodID;                                    // 5
        //uint32    FootprintTextureID;                         // 6
        //float     FootprintTextureLength;                     // 7
        //float     FootprintTextureWidth;                      // 8
        //float     FootprintParticleScale;                     // 9
        //uint32    FoleyMaterialID;                            // 10
        //uint32    FootstepShakeSize;                          // 11
        //uint32    DeathThudShakeSize;                         // 12
        //uint32    SoundID;                                    // 13
        //float     CollisionWidth;                             // 14
        float       CollisionHeight;                            // 15
        float       MountHeight;                                // 16
        //float     GeoBoxMin[3];                               // 17-19
        //float     GeoBoxMax[3];                               // 20-22
        //float     WorldEffectScale;                           // 23
        //float     AttachedEffectScale;                        // 24
        //float     MissileCollisionRadius;                     // 25
        //float     MissileCollisionPush;                       // 26
        //float     MissileCollisionRaise;                      // 27
        //float     OverrideLootEffectScale;                    // 28
        //float     OverrideNameScale;                          // 29
        //float     OverrideSelectionRadius;                    // 30
        //float     TamedPetBaseScale;                          // 31
        //uint32    CreatureGeosetDataID;                       // 32
        //float     HoverHeight;                                // 33
    };
    
    #define MAX_CREATURE_SPELL_DATA_SLOT 4
    
    struct CreatureSpellDataEntry
    {
        uint32      ID;                                         // 0
        uint32      Spells[MAX_CREATURE_SPELL_DATA_SLOT];       // 1-4
        //uint32    Availability[MAX_CREATURE_SPELL_DATA_SLOT]; // 4-7
    };
    
    struct CreatureTypeEntry
    {
        uint32      ID;                                         // 0
        //char*     Name_lang;                                  // 1
        //uint32    Flags;                                      // 2 no exp? critters, non-combat pets, gas cloud.
    };
    
    struct CriteriaEntry
    {
        uint32 ID;                                              // 0
        uint32 Type;                                            // 1
        union
        {
            uint32 ID;
            // ACHIEVEMENT_CRITERIA_TYPE_KILL_CREATURE          = 0
            // ACHIEVEMENT_CRITERIA_TYPE_KILLED_BY_CREATURE     = 20
            uint32 CreatureID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_WIN_BG                 = 1
            // ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_BATTLEGROUND  = 15
            // ACHIEVEMENT_CRITERIA_TYPE_DEATH_AT_MAP           = 16
            // ACHIEVEMENT_CRITERIA_TYPE_WIN_ARENA              = 32
            // ACHIEVEMENT_CRITERIA_TYPE_PLAY_ARENA             = 33
            uint32 MapID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_REACH_SKILL_LEVEL      = 7
            // ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILL_LEVEL      = 40
            // ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILLLINE_SPELLS = 75
            // ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILL_LINE       = 112
            uint32 SkillID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_ACHIEVEMENT   = 8
            uint32 AchievementID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUESTS_IN_ZONE = 11
            uint32 ZoneID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_CURRENCY = 12
            uint32 CurrencyID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_DEATH_IN_DUNGEON       = 18
            // ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_RAID          = 19
            uint32 GroupSize;
    
            // ACHIEVEMENT_CRITERIA_TYPE_DEATHS_FROM            = 26
            uint32 DamageType;
    
            // ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUEST         = 27
            uint32 QuestID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_BE_SPELL_TARGET        = 28
            // ACHIEVEMENT_CRITERIA_TYPE_BE_SPELL_TARGET2       = 69
            // ACHIEVEMENT_CRITERIA_TYPE_CAST_SPELL             = 29
            // ACHIEVEMENT_CRITERIA_TYPE_CAST_SPELL2            = 110
            // ACHIEVEMENT_CRITERIA_TYPE_LEARN_SPELL            = 34
            uint32 SpellID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_BG_OBJECTIVE_CAPTURE
            uint32 ObjectiveId;
    
            // ACHIEVEMENT_CRITERIA_TYPE_HONORABLE_KILL_AT_AREA = 31
            uint32 AreaID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_OWN_ITEM               = 36
            // ACHIEVEMENT_CRITERIA_TYPE_USE_ITEM               = 41
            // ACHIEVEMENT_CRITERIA_TYPE_LOOT_ITEM              = 42
            // ACHIEVEMENT_CRITERIA_TYPE_EQUIP_ITEM             = 57
            uint32 ItemID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_TEAM_RATING    = 38
            // ACHIEVEMENT_CRITERIA_TYPE_REACH_TEAM_RATING      = 39
            // ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_PERSONAL_RATING = 39
            uint32 TeamType;
    
            // ACHIEVEMENT_CRITERIA_TYPE_EXPLORE_AREA           = 43
            uint32 WorldMapOverlayID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_GAIN_REPUTATION        = 46
            uint32 FactionID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_EQUIP_EPIC_ITEM        = 49
            uint32 ItemSlot;
    
            // ACHIEVEMENT_CRITERIA_TYPE_ROLL_NEED_ON_LOOT      = 50
            // ACHIEVEMENT_CRITERIA_TYPE_ROLL_GREED_ON_LOOT      = 51
            uint32 RollValue;
    
            // ACHIEVEMENT_CRITERIA_TYPE_HK_CLASS               = 52
            uint32 ClassID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_HK_RACE                = 53
            uint32 RaceID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_DO_EMOTE               = 54
            uint32 EmoteID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_USE_GAMEOBJECT         = 68
            // ACHIEVEMENT_CRITERIA_TYPE_FISH_IN_GAMEOBJECT     = 72
            uint32 GameObjectID;
    
            // ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_POWER          = 96
            uint32 PowerType;
    
            // ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_STAT           = 97
            uint32 StatType;
    
            // ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_SPELLPOWER     = 98
            uint32 SpellSchool;
    
            // ACHIEVEMENT_CRITERIA_TYPE_LOOT_TYPE              = 109
            uint32 LootType;
        } Asset;                                                // 2
        uint32 StartEvent;                                      // 3
        uint32 StartAsset;                                      // 4
        uint32 StartTimer;                                      // 5
        uint32 FailEvent;                                       // 6
        uint32 FailAsset;                                       // 7
        uint32 ModifierTreeId;                                  // 8
        //uint32 Flags;                                         // 9
        uint32 EligibilityWorldStateID;                         // 10
        uint32 EligibilityWorldStateValue;                      // 11
    };
    
    struct CriteriaTreeEntry
    {
        uint32 ID;                                              // 0
        uint32 CriteriaID;                                      // 1
        uint64 Amount;                                          // 2
        uint32 Operator;                                        // 3
        uint32 Parent;                                          // 4
        //uint32 Flags;                                         // 5
        //char*  DescriptionLang;                               // 6
        //uint32 OrderIndex;                                    // 7
    };
    
    /* not used
    struct CurrencyCategoryEntry
    {
        uint32    ID;                                           // 0
        uint32    Unk1;                                         // 1        0 for known categories and 3 for unknown one (3.0.9)
        char*   Name[16];                                       // 2-17     name
        //                                                      // 18       string flags
    };
    */
    
    struct DestructibleModelDataEntry
    {
        uint32          ID;                                     // 0
        struct
        {
            uint32      DisplayID;                              // 1
            //uint32    ImpactEffectDoodadSet;                  // 2
            //uint32    AmbientDoodadSet;                       // 3
            //uint32    NameSet;                                // 4
        } StateDamaged;
        struct
        {
            uint32      DisplayID;                              // 5
            //uint32    DestructionDoodadSet;                   // 6
            //uint32    ImpactEffectDoodadSet;                  // 7
            //uint32    AmbientDoodadSet;                       // 8
            //uint32    NameSet;                                // 9
        } StateDestroyed;
        struct
        {
            uint32      DisplayID;                              // 10
            //uint32    DestructionDoodadSet;                   // 11
            //uint32    ImpactEffectDoodadSet;                  // 12
            //uint32    AmbientDoodadSet;                       // 13
            //uint32    NameSet;                                // 14
        } StateRebuilding;
        struct
        {
            uint32      DisplayID;                              // 15
            //uint32    InitDoodadSet;                          // 16
            //uint32    AmbientDoodadSet;                       // 17
            //uint32    NameSet;                                // 18
        } StateSmoke;
        //uint32        EjectDirection;                         // 19
        //uint32        RepairGroundFx;                         // 20
        //uint32        DoNotHighlight;                         // 21
        //uint32        HealEffect;                             // 22
        //uint32        HealEffectSpeed;                        // 23
    };
    
    struct DifficultyEntry
    {
        uint32      ID;                                         // 0
        uint32      FallbackDifficultyID;                       // 1
        uint32      InstanceType;                               // 2
        uint32      MinPlayers;                                 // 3
        uint32      MaxPlayers;                                 // 4
        //int32     OldEnumValue;                               // 5
        uint32      Flags;                                      // 6
        uint32      ToggleDifficultyID;                         // 7
        //uint32    GroupSizeHealthCurveID;                     // 8
        //uint32    GroupSizeDmgCurveID;                        // 9
        //uint32    GroupSizeSpellPointsCurveID;                // 10
        //char const* NameLang;                                 // 11
        uint32      ItemBonusTreeModID;                         // 12
    };
    
    struct DungeonEncounterEntry
    {
        uint32      ID;                                         // 0
        uint32      MapID;                                      // 1
        uint32      DifficultyID;                               // 2
        uint32      OrderIndex;                                 // 3
        //uint32    Bit;                                        // 4
        char*       Name_lang;                                  // 5
        //uint32    CreatureDisplayID;                          // 6
        //uint32    Flags;                                      // 7
        //uint32    Unk;                                        // 8 Flags2?
    };
    
    struct DurabilityCostsEntry
    {
        uint32      ID;                                         // 0
        uint32      WeaponSubClassCost[21];                     // 1-22
        uint32      ArmorSubClassCost[8];                       // 23-30
    };
    
    struct DurabilityQualityEntry
    {
        uint32      ID;                                         // 0
        float       QualityMod;                                 // 1
    };
    
    struct EmotesEntry
    {
        uint32      ID;                                         // 0
        //char*     EmoteSlashCommand;                          // 1
        //uint32    AnimID;                                     // 2 ref to animationData
        uint32      EmoteFlags;                                 // 3 bitmask, may be unit_flags
        uint32      EmoteSpecProc;                              // 4 Can be 0, 1 or 2 (determine how emote are shown)
        uint32      EmoteSpecProcParam;                         // 5 uncomfirmed, may be enum UnitStandStateType
        //uint32    EmoteSoundID;                               // 6 ref to soundEntries
        //uint32    SpellVisualKitID                            // 7
    };
    
    struct EmotesTextEntry
    {
        uint32      ID;                                         // 0
        //char*     Name_lang;                                  // 1
        uint32      EmoteID;                                    // 2
        //uint32    EmoteText[16];                              // 3-18
    };
    
    struct FactionEntry
    {
        uint32      ID;                                         // 0
        int32       ReputationIndex;                            // 1
        uint32      ReputationRaceMask[4];                      // 2-5
        uint32      ReputationClassMask[4];                     // 6-9
        int32       ReputationBase[4];                          // 10-13
        uint32      ReputationFlags[4];                         // 14-17
        uint32      ParentFactionID;                            // 18
        float       ParentFactionModIn;                         // 19 Faction gains incoming rep * ParentFactionModIn
        float       ParentFactionModOut;                        // 20 Faction outputs rep * ParentFactionModOut as spillover reputation
        uint32      ParentFactionCapIn;                         // 21 The highest rank the faction will profit from incoming spillover
        //uint32    ParentFactionCapOut;                        // 22
        char*       Name_lang;                                  // 23
        //char*     Description_lang;                           // 24
        uint32      Expansion;                                  // 25
        //uint32    Flags;                                      // 26
        //uint32    FriendshipRepID;                            // 27
    };
    
    #define MAX_FACTION_RELATIONS 4
    
    struct FactionTemplateEntry
    {
        uint32      ID;                                         // 0
        uint32      Faction;                                    // 1
        uint32      Flags;                                      // 2
        uint32      Mask;                                       // 3 m_factionGroup
        uint32      FriendMask;                                 // 4 m_friendGroup
        uint32      EnemyMask;                                  // 5 m_enemyGroup
        uint32      Enemies[MAX_FACTION_RELATIONS];             // 6
        uint32      Friends[MAX_FACTION_RELATIONS];             // 10
    };
    
    struct GameObjectDisplayInfoEntry
    {
        uint32          ID;                                     // 0
        uint32          FileDataID;                             // 1
        //uint32        Sound[10];                              // 2-11
        DBCPosition3D   GeoBoxMin;                              // 12-14
        DBCPosition3D   GeoBoxMax;                              // 15-17
        //uint32        ObjectEffectPackageID;                  // 18
        //float         OverrideLootEffectScale;                // 19
        //float         OverrideNameScale;                      // 20
    };
    
    struct GameTablesEntry
    {
        //uint32 Index;                                         // 0 - not a real field, not counted for columns
        char const* Name;                                       // 1
        uint32 NumRows;                                         // 2
        uint32 NumColumns;                                      // 3
    };
    
    struct GemPropertiesEntry
    {
        uint32      ID;                                         // 0
        uint32      EnchantID;                                  // 1
        //uint32    MaxCountInv;                                // 2
        //uint32    MaxCountItem;                               // 3
        uint32      Type;                                       // 4
        uint32      MinItemLevel;                               // 5
    };
    
    struct GlyphPropertiesEntry
    {
        uint32      ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      Type;                                       // 2
        uint32      SpellIconID;                                // 3 GlyphIconId (SpellIcon.dbc)
        //uint32    GlyphExclusiveCategoryID;                   // 4
    };
    
    struct GlyphSlotEntry
    {
        uint32      ID;                                         // 0
        uint32      Type;                                       // 1
        //uint32    Tooltip;                                    // 2
    };
    
    struct GtBarberShopCostBaseEntry
    {
        //uint32 level;
        float   cost;
    };
    
    struct GtCombatRatingsEntry
    {
        //uint32 level;
        float    ratio;
    };
    
    struct GtChanceToMeleeCritBaseEntry
    {
        //uint32 level;
        float    base;
    };
    
    struct GtChanceToMeleeCritEntry
    {
        //uint32 level;
        float    ratio;
    };
    
    struct GtChanceToSpellCritBaseEntry
    {
        float    base;
    };
    
    struct GtItemSocketCostPerLevelEntry
    {
        float    ratio;
    };
    
    struct GtNPCManaCostScalerEntry
    {
        float    ratio;
    };
    
    struct GtNpcTotalHpEntry
    {
        float    HP;
    };
    
    struct GtNpcTotalHpExp1Entry
    {
        float    HP;
    };
    
    struct GtNpcTotalHpExp2Entry
    {
        float    HP;
    };
    
    struct GtNpcTotalHpExp3Entry
    {
        float    HP;
    };
    
    struct GtNpcTotalHpExp4Entry
    {
        float    HP;
    };
    
    struct GtNpcTotalHpExp5Entry
    {
        float    HP;
    };
    
    struct GtChanceToSpellCritEntry
    {
        float    ratio;
    };
    
    struct GtOCTLevelExperienceEntry
    {
        float    Data;
    };
    
    struct GtOCTRegenHPEntry
    {
        float    ratio;
    };
    
    struct GtOCTRegenMPEntry
    {
        float    ratio;
    };
    
    struct gtOCTHpPerStaminaEntry
    {
        float    ratio;
    };
    
    struct GtRegenHPPerSptEntry
    {
        float    ratio;
    };
    
    struct GtRegenMPPerSptEntry
    {
        float    ratio;
    };
    
    struct GtSpellScalingEntry
    {
        float value;
    };
    
    struct GtOCTBaseHPByClassEntry
    {
        float ratio;
    };
    
    struct GtOCTBaseMPByClassEntry
    {
        float ratio;
    };
    
    struct GuildPerkSpellsEntry
    {
        //uint32    ID;                                         // 0
        uint32      GuildLevel;                                 // 1
        uint32      SpellID;                                    // 2
    };
    
    // ImportPriceArmor.dbc
    struct ImportPriceArmorEntry
    {
        uint32      ID;                                         // 1        Id/InventoryType
        float       ClothFactor;                                // 2        Price factor cloth
        float       LeatherFactor;                              // 3        Price factor leather
        float       MailFactor;                                 // 4        Price factor mail
        float       PlateFactor;                                // 5        Price factor plate
    };
    
    // ImportPriceQuality.dbc
    struct ImportPriceQualityEntry
    {
        uint32      ID;                                         // 1        Quality Id (+1?)
        float       Factor;                                     // 2        Price factor
    };
    
    // ImportPriceShield.dbc
    struct ImportPriceShieldEntry
    {
        uint32      ID;                                         // 1        Unk id (only 1 and 2)
        float       Factor;                                     // 2        Price factor
    };
    
    // ImportPriceWeapon.dbc
    struct ImportPriceWeaponEntry
    {
        uint32      ID;                                         // 1        Unk id (mainhand - 0, offhand - 1, weapon - 2, 2hweapon - 3, ranged/rangedright/relic - 4)
        float       Factor;                                     // 2        Price factor
    };
    
    // ItemPriceBase.dbc
    struct ItemPriceBaseEntry
    {
        uint32 ItemLevel;                                       // 2        Item level (1 - 1000)
        float ArmorFactor;                                      // 3        Price factor for armor
        float WeaponFactor;                                     // 4        Price factor for weapons
    };
    
    // common struct for:
    // ItemDamageAmmo.dbc
    // ItemDamageOneHand.dbc
    // ItemDamageOneHandCaster.dbc
    // ItemDamageRanged.dbc
    // ItemDamageThrown.dbc
    // ItemDamageTwoHand.dbc
    // ItemDamageTwoHandCaster.dbc
    // ItemDamageWand.dbc
    struct ItemDamageEntry
    {
        uint32      ID;                                         // 0 item level
        float       DPS[7];                                     // 1-7 multiplier for item quality
        uint32      ItemLevel;                                  // 8 item level
    };
    
    struct ItemArmorQualityEntry
    {
        uint32      ID;                                         // 0 item level
        float       QualityMod[7];                              // 1-7 multiplier for item quality
        uint32      ItemLevel;                                  // 8 item level
    };
    
    struct ItemArmorShieldEntry
    {
        uint32      ID;                                         // 0 item level
        uint32      ItemLevel;                                  // 1 item level
        float       Quality[7];                                 // 2-8 quality
    };
    
    struct ItemArmorTotalEntry
    {
        uint32      ID;                                         // 0 item level
        uint32      ItemLevel;                                  // 1 item level
        float       Value[4];                                   // 2-5 multiplier for armor types (cloth...plate)
    };
    
    // ItemClass.dbc
    struct ItemClassEntry
    {
        uint32      ID;                                          // 0 item class id
        //uint32    Flags;                                       // 1 Weapon - 1, others - 0
        float       PriceMod;                                    // 2 used to calculate certain prices
        //char*     Name_lang;                                   // 3 class name
    };
    
    struct ItemBagFamilyEntry
    {
        uint32      ID;                                         // 0
        //char*     Name_lang;                                  // 1        m_name_lang
    };
    
    struct ItemDisplayInfoEntry
    {
        uint32      ID;                                         // 0
        //char*     ModelName[2];                               // 1-2
        //char*     ModelTexture[2];                            // 3-4
        //uint32    GeoSetGroup[3];                             // 5-7
        //uint32    Flags;                                      // 8
        //uint32    SpellVisualID;                              // 9
        //uint32    HelmetGeosetVis[2];                         // 10-11
        //char*     Texture[9];                                 // 12-20
        //uint32    ItemVisual;                                 // 21
        //uint32    ParticleColorID;                            // 22
    };
    
    struct ItemDisenchantLootEntry
    {
        uint32      ID;                                         // 0
        uint32      ItemClass;                                  // 1
        int32       ItemSubClass;                               // 2
        uint32      ItemQuality;                                // 3
        uint32      MinItemLevel;                               // 4
        uint32      MaxItemLevel;                               // 5
        uint32      RequiredDisenchantSkill;                    // 6
    };
    
    struct ItemLimitCategoryEntry
    {
        uint32      ID;                                         // 0 Id
        //char*     Name_lang;                                  // 1        m_name_lang
        uint32      Quantity;                                   // 2,       m_quantity max allowed equipped as item or in gem slot
        uint32      Flags;                                      // 3,       m_flags 0 = have, 1 = equip (enum ItemLimitCategoryMode)
    };
    
    #define MAX_ITEM_RANDOM_PROPERTIES 5
    
    struct ItemRandomPropertiesEntry
    {
        uint32      ID;                                         // 0
        //char*     Name;                                       // 1
        uint32      Enchantment[MAX_ITEM_RANDOM_PROPERTIES];  // 2-6
        char*       Name_lang;                                  // 7
    };
    
    struct ItemRandomSuffixEntry
    {
        uint32      ID;                                         // 0
        char*       Name_lang;                                  // 1
        //char*     InternalName;                               // 2
        uint32      Enchantment[MAX_ITEM_RANDOM_PROPERTIES];  // 3-7
        uint32      AllocationPct[MAX_ITEM_RANDOM_PROPERTIES];// 8-12
    };
    
    #define MAX_ITEM_SET_ITEMS 17
    #define MAX_ITEM_SET_SPELLS 8
    
    struct ItemSetEntry
    {
        uint32      ID;                                         // 0
        char*       Name_lang;                                  // 1
        uint32      ItemID[MAX_ITEM_SET_ITEMS];                 // 2-18
        uint32      RequiredSkill;                              // 19
        uint32      RequiredSkillRank;                          // 20
    };
    
    struct ItemSetSpellEntry
    {
        uint32      ID;                                         // 0
        uint32      ItemSetID;                                  // 1
        uint32      SpellID;                                    // 2
        uint32      Threshold;                                  // 3
        uint32      ChrSpecID;                                  // 4
    };
    
    typedef std::vector<ItemSetSpellEntry const*> ItemSetSpells;
    typedef std::unordered_map<uint32, ItemSetSpells> ItemSetSpellsStore;
    
    struct LFGDungeonEntry
    {
        uint32      ID;                                         // 0
        char*       Name_lang;                                  // 1
        uint32      MinLevel;                                   // 2
        uint32      MaxLevel;                                   // 3
        uint32      TargetLevel;                                // 4
        //uint32    TargetLevelMin;                             // 5
        //uint32    TargetLevelMax;                             // 6
        int32       MapID;                                      // 7
        uint32      DifficultyID;                               // 8
        uint32      Flags;                                      // 9
        uint32      Type;                                       // 10
        //uint32    Faction;                                    // 11
        //char*     TextureFilename;                            // 12
        uint32      Expansion;                                  // 13
        //uint32    OrderIndex;                                 // 14
        uint32      GroupID;                                    // 15
        //char*     Description_lang;                           // 16
        //uint32    RandomID;                                   // 17
        //uint32    CountTank;                                  // 18
        //uint32    CountHealer;                                // 19
        //uint32    CountDamage;                                // 20
        //uint32    ScenarioID;                                 // 21
        //uint32    SubType;                                    // 22
        //uint32    BonusReputationAmount;                      // 23
        //uint32    MentorCharLevel;                            // 24
        //uint32    MentorItemLevel;                            // 25
        //uint32    Unk1;                                       // 26 (300 for random dungeons, others 0)
        //uint32    Unk2;                                       // 27
        //uint32    Unk3;                                       // 28
    };
    
    struct LightEntry
    {
        uint32          ID;                                     // 0
        uint32          MapID;                                  // 1
        DBCPosition3D   Pos;                                    // 2-4
        //float         FalloffStart;                           // 5
        //float         FalloffEnd;                             // 6
        //uint32        LightParamsID[8];                       // 7-14
    };
    
    struct LiquidTypeEntry
    {
        uint32      ID;                                         // 0
        //char*     Name;                                       // 1
        //uint32    Flags;                                      // 2
        uint32      Type;                                       // 3 m_soundBank
        //uint32    SoundID;                                    // 4
        uint32      SpellID;                                    // 5
        //float     MaxDarkenDepth;                             // 6
        //float     FogDarkenIntensity;                         // 7
        //float     AmbDarkenIntensity;                         // 8
        //float     DirDarkenIntensity;                         // 9
        //uint32    LightID;                                    // 10
        //float     ParticleScale;                              // 11
        //uint32    ParticleMovement;                           // 12
        //uint32    ParticleTexSlots;                           // 13
        //uint32    MaterialID;                                 // 14
        //char*     Texture[6];                                 // 15-20
        //uint32    Color[2];                                   // 21-23
        //float     Unk1[18];                                   // 24-41
        //uint32    Unk2[4];                                    // 42-45
        //uint32    Unk3[5];                                    // 46-50
    };
    
    #define MAX_LOCK_CASE 8
    
    struct LockEntry
    {
        uint32      ID;                                         // 0
        uint32      Type[MAX_LOCK_CASE];                        // 1-8
        uint32      Index[MAX_LOCK_CASE];                       // 9-16
        uint32      Skill[MAX_LOCK_CASE];                       // 17-24
        //uint32    Action[MAX_LOCK_CASE];                      // 25-32
    };
    
    struct PhaseEntry
    {
        uint32      ID;                                         // 0
        uint32      Flags;                                      // 1
    };
    
    struct MailTemplateEntry
    {
        uint32      ID;                                         // 0
        //char*     Subject_lang;                               // 1
        char*       Body_lang;                                  // 2
    };
    
    struct MapEntry
    {
        uint32          ID;                                     // 0
        //char*         Directory;                              // 1
        uint32          InstanceType;                           // 2
        uint32          Flags;                                  // 3
        //uint32        MapType;                                // 4
        //uint32        unk5;                                   // 5
        char*           MapName_lang;                           // 6
        uint32          AreaTableID;                            // 7
        //char*         MapDescription0_lang;                   // 8 Horde
        //char*         MapDescription1_lang;                   // 9 Alliance
        uint32          LoadingScreenID;                        // 10 LoadingScreens.dbc
        //float         MinimapIconScale;                       // 11
        int32           CorpseMapID;                            // 12 map_id of entrance map in ghost mode (continent always and in most cases = normal entrance)
        DBCPosition2D   CorpsePos;                              // 13 entrance coordinates in ghost mode  (in most cases = normal entrance)
        //uint32        TimeOfDayOverride;                      // 15
        uint32          ExpansionID;                            // 16
        uint32          RaidOffset;                             // 17
        uint32          MaxPlayers;                             // 18
        int32           ParentMapID;                            // 19 related to phasing
        //uint32        CosmeticParentMapID                     // 20
        //uint32        TimeOffset                              // 21
    };
    
    struct MapDifficultyEntry
    {
        //uint32    ID;                                         // 0
        uint32      MapID;                                      // 1
        uint32      DifficultyID;                               // 2 (for arenas: arena slot)
        char*       Message_lang;                               // 3 m_message_lang (text showed when transfer to map failed)
        uint32      RaidDuration;                               // 4 m_raidDuration in secs, 0 if no fixed reset time
        uint32      MaxPlayers;                                 // 5 m_maxPlayers some heroic versions have 0 when expected same amount as in normal version
        uint32      LockID;                                     // 6
        uint32      ItemBonusTreeModID;                         // 7
    
        bool HasMessage() const { return Message_lang[0] != '\0'; }
    };
    
    struct MinorTalentEntry
    {
        uint32      ID;                                         // 0
        uint32      SpecID;                                     // 1
        uint32      SpellID;                                    // 2
        uint32      OrderIndex;                                 // 3
    };
    
    struct ModifierTreeEntry
    {
        uint32      ID;                                         // 0
        uint32      Type;                                       // 1
        uint32      Asset[2];                                   // 2-3
        uint32      Operator;                                   // 4
        uint32      Amount;                                     // 5
        uint32      Parent;                                     // 6
    };
    
    struct MountCapabilityEntry
    {
        uint32      ID;                                         // 0
        uint32      Flags;                                      // 1
        uint32      RequiredRidingSkill;                        // 2
        uint32      RequiredArea;                               // 3
        uint32      RequiredAura;                               // 4
        uint32      RequiredSpell;                              // 5
        uint32      SpeedModSpell;                              // 6
        int32       RequiredMap;                                // 7
    };
    
    #define MAX_MOUNT_CAPABILITIES 24
    
    struct MountTypeEntry
    {
        uint32      ID;                                         // 0
        uint32      MountCapability[MAX_MOUNT_CAPABILITIES];    // 1-24
    };
    
    struct MovieEntry
    {
        uint32      ID;                                         // 0 index
        //uint32    Volume;                                     // 1
        //uint32    KeyID;                                      // 2
        //uint32    AudioFileDataID;                            // 3
        //uint32    SubtitleFileDataID;                         // 4
    };
    
    struct NameGenEntry
    {
        //uint32    ID;                                         // 0
        char*       Name;                                       // 1
        uint32      Race;                                       // 2
        uint32      Sex;                                        // 3
    };
    
    struct PowerDisplayEntry
    {
        uint32      ID;                                         // 0
        uint32      PowerType;                                  // 1
        //char*     GlobalStringBaseTag;                        // 2
        //uint8     Red;                                        // 3
        //uint8     Green;                                      // 4
        //uint8     Blue;                                       // 5
    };
    
    struct PvPDifficultyEntry
    {
        //uint32    ID;                                         // 0
        uint32      MapID;                                      // 1
        uint32      BracketID;                                  // 2 m_rangeIndex
        uint32      MinLevel;                                   // 3
        uint32      MaxLevel;                                   // 4
    
        // helpers
        BattlegroundBracketId GetBracketId() const { return BattlegroundBracketId(BracketID); }
    };
    
    struct QuestSortEntry
    {
        uint32      ID;                                         // 0
        //char*     SortName_lang;                              // 1
    };
    
    struct QuestV2Entry
    {
        uint32      ID;                                         // 0
        uint32      UniqueBitFlag;                              // 1
    };
    
    struct QuestXPEntry
    {
        uint32      ID;                                         // 0
        uint32      Exp[10];                                    // 1
    };
    
    struct QuestFactionRewEntry
    {
      uint32        ID;                                         // 0
      int32         QuestRewFactionValue[10];                   // 1-10
    };
    
    struct RandomPropertiesPointsEntry
    {
        uint32      ItemLevel;                                  // 0
        uint32      EpicPropertiesPoints[5];                    // 1-5
        uint32      RarePropertiesPoints[5];                    // 6-10
        uint32      UncommonPropertiesPoints[5];                // 11-15
    };
    
    struct ScalingStatDistributionEntry
    {
        uint32      ID;                                         // 0
        uint32      MinLevel;                                   // 1
        uint32      MaxLevel;                                   // 2       m_maxlevel
        uint32      ItemLevelCurveID;                           // 3
    };
    
    //struct SkillLineCategoryEntry{
    //    uint32    id;                                         // 0      m_ID
    //    char*     name[16];                                   // 1-17   m_name_lang
    //                                                          // 18 string flag
    //    uint32    displayOrder;                               // 19     m_sortIndex
    //};
    
    struct SkillLineEntry
    {
        uint32      ID;                                         // 0        m_ID
        int32       CategoryID;                                 // 1        m_categoryID
        char*       DisplayName_lang;                           // 2        m_displayName_lang
        //char*     Description_lang;                           // 3        m_description_lang
        uint32      SpellIconID;                                // 4        m_spellIconID
        //char*     AlternateVerb_lang;                         // 5        m_alternateVerb_lang
        uint32      CanLink;                                    // 6        m_canLink (prof. with recipes)
        //uint32    ParentSkillLineID;                          // 7
        //uint32    Flags;                                      // 8
    };
    
    struct SkillLineAbilityEntry
    {
        uint32      ID;                                         // 0
        uint32      SkillLine;                                  // 1
        uint32      SpellID;                                    // 2
        uint32      RaceMask;                                   // 3
        uint32      ClassMask;                                  // 4
        uint32      MinSkillLineRank;                           // 7
        uint32      SupercedesSpell;                            // 8
        uint32      AquireMethod;                               // 9
        uint32      TrivialSkillLineRankHigh;                   // 10
        uint32      TrivialSkillLineRankLow;                    // 11
        uint32      NumSkillUps;                                // 12
        uint32      UniqueBit;                                  // 13
        uint32      TradeSkillCategoryID;                       // 14
    };
    
    struct SkillRaceClassInfoEntry
    {
        //uint32    ID;                                         // 0
        uint32      SkillID;                                    // 1
        int32       RaceMask;                                   // 2
        int32       ClassMask;                                  // 3
        uint32      Flags;                                      // 4
        uint32      Availability;                               // 5
        uint32      MinLevel;                                   // 6
        uint32      SkillTierID;                                // 7
    };
    
    #define MAX_SKILL_STEP 16
    
    struct SkillTiersEntry
    {
        uint32      ID;                                         // 0
        uint32      Value[MAX_SKILL_STEP];                      // 1-16
    };
    
    // SpecializationSpells.dbc
    struct SpecializationSpellsEntry
    {
        uint32      ID;                                         // 0
        uint32      SpecID;                                     // 1
        uint32      SpellID;                                    // 2
        uint32      OverridesSpellID;                           // 3
        //char*     Description_lang;                           // 4
    };
    
    // SpellEffect.dbc
    struct SpellEffectEntry
    {
        uint32      ID;                                         // 0
        uint32      DifficultyID;                               // 1
        uint32      Effect;                                     // 2
        float       EffectAmplitude;                            // 3
        uint32      EffectAura;                                 // 4
        uint32      EffectAuraPeriod;                           // 5
        uint32      EffectBasePoints;                           // 6
        float       EffectBonusCoefficient;                     // 7
        float       EffectChainAmplitude;                       // 8
        uint32      EffectChainTargets;                         // 9
        uint32      EffectDieSides;                             // 10
        uint32      EffectItemType;                             // 11
        uint32      EffectMechanic;                             // 12
        int32       EffectMiscValue;                            // 13
        int32       EffectMiscValueB;                           // 14
        float       EffectPointsPerResource;                    // 15
        uint32      EffectRadiusIndex;                          // 16
        uint32      EffectRadiusMaxIndex;                       // 17
        float       EffectRealPointsPerLevel;                   // 18
        flag128     EffectSpellClassMask;                       // 19-22
        uint32      EffectTriggerSpell;                         // 23
        float       EffectPosFacing;                            // 24
        uint32      ImplicitTarget[2];                          // 25-26
        uint32      SpellID;                                    // 27
        uint32      EffectIndex;                                // 28
        uint32      EffectAttributes;                           // 29
        float       BonusCoefficientFromAP;                     // 30
    };
    
    #define MAX_SPELL_EFFECTS 32
    #define MAX_EFFECT_MASK 0xFFFFFFFF
    
    // SpellEffectScaling.dbc
    struct SpellEffectScalingEntry
    {
        uint32 ID;                      // 0
        float Coefficient;              // 1
        float Variance;                 // 2
        float ResourceCoefficient;      // 3
        uint32 SpellEffectID;           // 4
    };
    
    // SpellAuraOptions.dbc
    struct SpellAuraOptionsEntry
    {
        uint32      ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      DifficultyID;                               // 2
        uint32      CumulativeAura;                             // 3
        uint32      ProcChance;                                 // 4
        uint32      ProcCharges;                                // 5
        uint32      ProcTypeMask;                               // 6
        uint32      ProcCategoryRecovery;                       // 7
        uint32      SpellProcsPerMinuteID;                      // 8
    };
    
    // Spell.dbc
    struct SpellEntry
    {
        uint32      ID;                                         // 0
        char*       Name_lang;                                  // 1
        //char*     NameSubtext_lang;                           // 2
        //char*     Description_lang;                           // 3
        //char*     AuraDescription_lang;                       // 4
        uint32      RuneCostID;                                 // 5
        uint32      SpellMissileID;                             // 6
        uint32      DescriptionVariablesID;                     // 7
        uint32      ScalingID;                                  // 8
        uint32      AuraOptionsID;                              // 9
        uint32      AuraRestrictionsID;                         // 10
        uint32      CastingRequirementsID;                      // 11
        uint32      CategoriesID;                               // 12
        uint32      ClassOptionsID;                             // 13
        uint32      CooldownsID;                                // 14
        uint32      EquippedItemsID;                            // 15
        uint32      InterruptsID;                               // 16
        uint32      LevelsID;                                   // 17
        uint32      ReagentsID;                                 // 18
        uint32      ShapeshiftID;                               // 19
        uint32      TargetRestrictionsID;                       // 20
        uint32      TotemsID;                                   // 21
        uint32      RequiredProjectID;                          // 22
        uint32      MiscID;                                     // 23
    };
    
    // SpellCategories.dbc
    struct SpellCategoriesEntry
    {
        //uint32    ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      DifficultyID;                               // 2
        uint32      Category;                                   // 3
        uint32      DefenseType;                                // 4
        uint32      DispelType;                                 // 5
        uint32      Mechanic;                                   // 6
        uint32      PreventionType;                             // 7
        uint32      StartRecoveryCategory;                      // 8
        uint32      ChargeCategory;                             // 9
    };
    
    typedef std::set<uint32> SpellCategorySet;
    typedef std::map<uint32, SpellCategorySet > SpellCategoryStore;
    typedef std::set<uint32> PetFamilySpellsSet;
    typedef std::map<uint32, PetFamilySpellsSet > PetFamilySpellsStore;
    typedef std::unordered_map<uint32, uint32> SpellEffectScallingByEffectId;
    
    struct SpellCastTimesEntry
    {
        uint32      ID;                                         // 0
        int32       CastTime;                                   // 1
        //float     CastTimePerLevel;                           // 2 unsure / per skill?
        //int32     MinCastTime;                                // 3 unsure
    };
    
    struct SpellCategoryEntry
    {
        uint32      ID;                                         // 0
        uint32      Flags;                                      // 1
        //uint8     UsesPerWeek;                                // 2
        //uint8     Padding[3];                                 // 2
        //char*     Name_lang;                                  // 3
        int32       MaxCharges;                                 // 4
        int32       ChargeRecoveryTime;                         // 5
    };
    
    struct SpellFocusObjectEntry
    {
        uint32      ID;                                         // 0
        //char*     Name_lang;                                  // 1
    };
    
    struct SpellRadiusEntry
    {
        uint32      ID;                                         // 0
        //float     Radius;                                     // 1
        float       RadiusPerLevel;                             // 2
        float       RadiusMin;                                  // 3
        float       RadiusMax;                                  // 4
    };
    
    struct SpellRangeEntry
    {
        uint32      ID;                                         // 0
        float       MinRangeHostile;                            // 1
        float       MinRangeFriend;                             // 2
        float       MaxRangeHostile;                            // 3
        float       MaxRangeFriend;                             // 4 friend means unattackable unit here
        uint32      Flags;                                      // 5
        //char*     DisplayName_lang;                           // 6
        //char*     DisplayNameShort_lang;                      // 7
    };
    
    // SpellEquippedItems.dbc
    struct SpellEquippedItemsEntry
    {
        //uint32    ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      DifficultyID;                               // 2
        int32       EquippedItemClass;                          // 3       m_equippedItemClass (value)
        int32       EquippedItemInventoryTypeMask;              // 4       m_equippedItemInvTypes (mask)
        int32       EquippedItemSubClassMask;                   // 5       m_equippedItemSubclass (mask)
    };
    
    // SpellCooldowns.dbc
    struct SpellCooldownsEntry
    {
        //uint32    ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      DifficultyID;                               // 2
        uint32      CategoryRecoveryTime;                       // 3
        uint32      RecoveryTime;                               // 4
        uint32      StartRecoveryTime;                          // 5
    };
    
    // SpellInterrupts.dbc
    struct SpellInterruptsEntry
    {
        //uint32    ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      DifficultyID;                               // 2
        uint32      AuraInterruptFlags[2];                      // 3-4
        uint32      ChannelInterruptFlags[2];                   // 5-6
        uint32      InterruptFlags;                             // 7
    };
    
    // SpellLevels.dbc
    struct SpellLevelsEntry
    {
        //uint32    ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      DifficultyID;                               // 2
        uint32      BaseLevel;                                  // 3
        uint32      MaxLevel;                                   // 4
        uint32      SpellLevel;                                 // 5
    };
    
    #define MAX_SHAPESHIFT_SPELLS 8
    
    struct SpellShapeshiftFormEntry
    {
        uint32      ID;                                         // 0
        //uint32    BonusActionBar;                             // 1
        //char*     Name_lang;                                  // 2
        uint32      Flags;                                      // 3
        int32       CreatureType;                               // 4 <=0 humanoid, other normal creature types
        //uint32    AttackIconID;                               // 5 unused, related to next field
        uint32      CombatRoundTime;                            // 6
        uint32      CreatureDisplayID[4];                       // 7-10 (0 - Alliance, 1 - Horde)
        uint32      PresetSpellID[MAX_SHAPESHIFT_SPELLS];       // 11-18 spells which appear in the bar after shapeshifting
        //uint32    MountTypeID;                                // 19
        //uint32    ExitSoundEntriesID;                         // 20
    };
    
    // SpellShapeshift.dbc
    struct SpellShapeshiftEntry
    {
        uint32      ID;                                         // 0
        uint32      ShapeshiftExclude[2];                       // 1-2
        uint32      ShapeshiftMask[2];                          // 3-4
        //uint32    StanceBarOrder;                             // 5
    };
    
    // SpellTargetRestrictions.dbc
    struct SpellTargetRestrictionsEntry
    {
        uint32      ID;                                         // 0
        uint32      SpellID;                                    // 1
        uint32      DifficultyID;                               // 2
        float       ConeAngle;                                  // 3
        float       Width;                                      // 4
        uint32      MaxAffectedTargets;                         // 5
        uint32      MaxTargetLevel;                             // 6
        uint32      TargetCreatureType;                         // 7
        uint32      Targets;                                    // 8
    };
    
    // SpellScaling.dbc
    struct SpellScalingEntry
    {
        uint32      ID;                                         // 0
        int32       CastTimeMin;                                // 1
        int32       CastTimeMax;                                // 2
        uint32      CastTimeMaxLevel;                           // 3
        int32       ScalingClass;                               // 4
        float       NerfFactor;                                 // 5
        uint32      NerfMaxLevel;                               // 6
        uint32      MaxScalingLevel;                            // 7
        uint32      ScalesFromItemLevel;                        // 8
    };
    
    struct SpellDurationEntry
    {
        uint32      ID;
        int32       Duration[3];
    };
    
    #define MAX_ITEM_ENCHANTMENT_EFFECTS 3
    
    struct SpellItemEnchantmentEntry
    {
        uint32      ID;                                             // 0
        uint32      Charges;                                        // 1
        uint32      Effect[MAX_ITEM_ENCHANTMENT_EFFECTS];           // 2-4
        uint32      EffectPointsMin[MAX_ITEM_ENCHANTMENT_EFFECTS];  // 5-7
        uint32      EffectSpellID[MAX_ITEM_ENCHANTMENT_EFFECTS];    // 8-10
        //char*     Name_lang                                       // 11
        uint32      ItemVisual;                                     // 12
        uint32      Flags;                                          // 13
        uint32      SRCItemID;                                      // 14
        uint32      ConditionID;                                    // 15
        uint32      RequiredSkillID;                                // 16
        uint32      RequiredSkillRank;                              // 17
        uint32      MinLevel;                                       // 18
        uint32      MaxLevel;                                       // 19
        uint32      ItemLevel;                                      // 20
        int32       ScalingClass;                                   // 21
        int32       ScalingClassRestricted;                         // 22
        float       EffectScalingPoints[MAX_ITEM_ENCHANTMENT_EFFECTS];//23-25
    };
    
    struct SpellItemEnchantmentConditionEntry
    {
        uint32      ID;                                             // 0
        uint8       LTOperandType[5];                               // 1-2
        uint32      LTOperand[5];                                   // 2-6
        uint8       Operator[5];                                    // 7-8
        uint8       RTOperandType[5];                               // 8-9
        uint32      RTOperand[5];                                   // 10-14
        uint8       Logic[5];                                       // 15-16
    };
    
    struct StableSlotPricesEntry
    {
        uint32 Slot;
        uint32 Price;
    };
    
    struct SummonPropertiesEntry
    {
        uint32      ID;                                             // 0
        uint32      Category;                                       // 1, 0 - can't be controlled?, 1 - something guardian?, 2 - pet?, 3 - something controllable?, 4 - taxi/mount?
        uint32      Faction;                                        // 2, 14 rows > 0
        uint32      Type;                                           // 3, see enum
        int32       Slot;                                           // 4, 0-6
        uint32      Flags;                                          // 5
    };
    
    #define MAX_TALENT_TIERS 7
    #define MAX_TALENT_COLUMNS 3
    
    struct TalentEntry
    {
        uint32      ID;                                             // 0
        uint32      SpecID;                                         // 1 0 - any specialization
        uint32      TierID;                                         // 2 0-6
        uint32      ColumnIndex;                                    // 3 0-2
        uint32      SpellID;                                        // 4
        uint32      Flags;                                          // 5 All 0
        uint32      CategoryMask[2];                                // 6 All 0
        uint32      ClassID;                                        // 7
        uint32      OverridesSpellID;                               // 8 spellid that is replaced by talent
        //char*     Description_lang
    };
    
    struct TotemCategoryEntry
    {
        uint32    ID;                                           // 0
        //char*   Name_lang;                                    // 1        m_name_lang
        uint32    CategoryType;                                 // 2        m_totemCategoryType (one for specialization)
        uint32    CategoryMask;                                 // 3        m_totemCategoryMask (compatibility mask for same type: different for totems, compatible from high to low for rods)
    };
    
    struct UnitPowerBarEntry
    {
        uint32      ID;                                         // 0
        uint32      MinPower;                                   // 1
        uint32      MaxPower;                                   // 2
        //uint32    StartPower;                                 // 3
        //uint32    CenterPower;                                // 4
        //float     RegenerationPeace;                          // 5
        //float     RegenerationCombat;                         // 6
        //uint32    BarType;                                    // 7
        //uint32    FileDataID[6];                              // 8-13
        //uint32    Color[6];                                   // 14-19
        //uint32    Flags;                                      // 20
        //char*     Name_lang;                                  // 21
        //char*     Cost_lang;                                  // 22
        //char*     OutOfError_lang;                            // 23
        //char*     ToolTip_lang;                               // 24
        //float     StartInset;                                 // 25
        //float     EndInset;                                   // 26
    };
    
    struct TransportAnimationEntry
    {
        //uint32        ID;                                     // 0
        uint32          TransportID;                            // 1
        uint32          TimeIndex;                              // 2
        DBCPosition3D   Pos;                                    // 3-5
        //uint32        SequenceID;                             // 6
    };
    
    struct TransportRotationEntry
    {
        //uint32    ID;                                         // 0
        uint32      TransportID;                                // 1
        uint32      TimeIndex;                                  // 2
        float       X;                                          // 3
        float       Y;                                          // 4
        float       Z;                                          // 5
        float       W;                                          // 6
    };
    
    #define MAX_VEHICLE_SEATS 8
    
    struct VehicleEntry
    {
        uint32      ID;                                         // 0
        uint32      Flags;                                      // 1
        uint32      FlagsB;                                     // 2
        float       TurnSpeed;                                  // 3
        float       PitchSpeed;                                 // 4
        float       PitchMin;                                   // 5
        float       PitchMax;                                   // 6
        uint32      SeatID[MAX_VEHICLE_SEATS];                  // 7-14
        float       MouseLookOffsetPitch;                       // 15
        float       CameraFadeDistScalarMin;                    // 16
        float       CameraFadeDistScalarMax;                    // 17
        float       CameraPitchOffset;                          // 18
        float       FacingLimitRight;                           // 19
        float       FacingLimitLeft;                            // 20
        float       MsslTrgtTurnLingering;                      // 21
        float       MsslTrgtPitchLingering;                     // 22
        float       MsslTrgtMouseLingering;                     // 23
        float       MsslTrgtEndOpacity;                         // 24
        float       MsslTrgtArcSpeed;                           // 25
        float       MsslTrgtArcRepeat;                          // 26
        float       MsslTrgtArcWidth;                           // 27
        float       MsslTrgtImpactRadius[2];                    // 28-29
        //char*     MsslTrgtArcTexture;                         // 30
        //char*     MsslTrgtImpactTexture;                      // 31
        //char*     MsslTrgtImpactModel;                        // 32-33
        float       CameraYawOffset;                            // 34
        uint32      UILocomotionType;                           // 35
        float       MsslTrgtImpactTexRadius;                    // 36
        uint32      VehicleUIIndicatorID;                       // 37
        uint32      PowerDisplayID[3];                          // 38-40
    };
    
    struct VehicleSeatEntry
    {
        uint32          ID;                                     // 0
        uint32          Flags;                                  // 1
        int32           AttachmentID;                           // 2
        DBCPosition3D   AttachmentOffset;                       // 3-5
        float           EnterPreDelay;                          // 6
        float           EnterSpeed;                             // 7
        float           EnterGravity;                           // 8
        float           EnterMinDuration;                       // 9
        float           EnterMaxDuration;                       // 10
        float           EnterMinArcHeight;                      // 11
        float           EnterMaxArcHeight;                      // 12
        int32           EnterAnimStart;                         // 13
        int32           EnterAnimLoop;                          // 14
        int32           RideAnimStart;                          // 15
        int32           RideAnimLoop;                           // 16
        int32           RideUpperAnimStart;                     // 17
        int32           RideUpperAnimLoop;                      // 18
        float           ExitPreDelay;                           // 19
        float           ExitSpeed;                              // 20
        float           ExitGravity;                            // 21
        float           ExitMinDuration;                        // 22
        float           ExitMaxDuration;                        // 23
        float           ExitMinArcHeight;                       // 24
        float           ExitMaxArcHeight;                       // 25
        int32           ExitAnimStart;                          // 26
        int32           ExitAnimLoop;                           // 27
        int32           ExitAnimEnd;                            // 28
        float           PassengerYaw;                           // 29
        float           PassengerPitch;                         // 30
        float           PassengerRoll;                          // 31
        int32           PassengerAttachmentID;                  // 32
        int32           VehicleEnterAnim;                       // 33
        int32           VehicleExitAnim;                        // 34
        int32           VehicleRideAnimLoop;                    // 35
        int32           VehicleEnterAnimBone;                   // 36
        int32           VehicleExitAnimBone;                    // 37
        int32           VehicleRideAnimLoopBone;                // 38
        float           VehicleEnterAnimDelay;                  // 39
        float           VehicleExitAnimDelay;                   // 40
        uint32          VehicleAbilityDisplay;                  // 41
        uint32          EnterUISoundID;                         // 42
        uint32          ExitUISoundID;                          // 43
        uint32          FlagsB;                                 // 44
        float           CameraEnteringDelay;                    // 45
        float           CameraEnteringDuration;                 // 46
        float           CameraExitingDelay;                     // 47
        float           CameraExitingDuration;                  // 48
        DBCPosition3D   CameraOffset;                           // 49-51
        float           CameraPosChaseRate;                     // 52
        float           CameraFacingChaseRate;                  // 53
        float           CameraEnteringZoom;                     // 54
        float           CameraSeatZoomMin;                      // 55
        float           CameraSeatZoomMax;                      // 56
        uint32          EnterAnimKitID;                         // 57
        uint32          RideAnimKitID;                          // 58
        uint32          ExitAnimKitID;                          // 59
        uint32          VehicleEnterAnimKitID;                  // 60
        uint32          VehicleRideAnimKitID;                   // 61
        uint32          VehicleExitAnimKitID;                   // 62
        uint32          CameraModeID;                           // 63
        uint32          FlagsC;                                 // 64
        uint32          UISkinFileDataID;                       // 65
    
        bool CanEnterOrExit() const { return (Flags & VEHICLE_SEAT_FLAG_CAN_ENTER_OR_EXIT) != 0; }
        bool CanSwitchFromSeat() const { return (Flags & VEHICLE_SEAT_FLAG_CAN_SWITCH) != 0; }
        bool IsUsableByOverride() const { return (Flags & (VEHICLE_SEAT_FLAG_UNCONTROLLED | VEHICLE_SEAT_FLAG_UNK18)
                                        || (FlagsB & (VEHICLE_SEAT_FLAG_B_USABLE_FORCED | VEHICLE_SEAT_FLAG_B_USABLE_FORCED_2 |
                                            VEHICLE_SEAT_FLAG_B_USABLE_FORCED_3 | VEHICLE_SEAT_FLAG_B_USABLE_FORCED_4))); }
        bool IsEjectable() const { return (FlagsB & VEHICLE_SEAT_FLAG_B_EJECTABLE) != 0; }
    };
    
    struct WMOAreaTableEntry
    {
        uint32      ID;                                         // 0 index
        int32       WMOID;                                      // 1 used in root WMO
        int32       NameSet;                                    // 2 used in adt file
        int32       WMOGroupID;                                 // 3 used in group WMO
        //uint32    SoundProviderPref;                          // 4
        //uint32    SoundProviderPrefUnderwater;                // 5
        //uint32    AmbienceID;                                 // 6
        //uint32    ZoneMusic;                                  // 7
        //uint32    IntroSound;                                 // 8
        uint32      Flags;                                      // 9 used for indoor/outdoor determination
        uint32      AreaTableID;                                // 10 link to AreaTableEntry.ID
        //char*     AreaName_lang;                              // 11       m_AreaName_lang
        //uint32    UWIntroSound;                               // 12
        //uint32    UWZoneMusic;                                // 13
        //uint32    UWAmbience;                                 // 14
    };
    
    struct WorldMapAreaEntry
    {
        //uint32    ID;                                         // 0
        uint32      MapID;                                      // 1
        uint32      AreaID;                                     // 2 index (continent 0 areas ignored)
        //char*     AreaName                                    // 3
        float       LocLeft;                                    // 4
        float       LocRight;                                   // 5
        float       LocTop;                                     // 6
        float       LocBottom;                                  // 7
        int32       DisplayMapID;                               // 8 -1 (map_id have correct map) other: virtual map where zone show (map_id - where zone in fact internally)
        //int32     DefaultDungeonFloor;                        // 9 pointer to DungeonMap.dbc (owerride loc coordinates)
        //uint32    ParentWorldMapID;                           // 10
        //uint32    Flags;                                      // 11
        //uint32    LevelRangeMin;                              // 12 Minimum recommended level displayed on world map
        //uint32    LevelRangeMax;                              // 13 Maximum recommended level displayed on world map
    };
    
    #define MAX_WORLD_MAP_OVERLAY_AREA_IDX 4
    
    struct WorldMapOverlayEntry
    {
        uint32      ID;                                         // 0
        //uint32    MapAreaID;                                  // 1 idx in WorldMapArea.dbc
        uint32      AreaID[MAX_WORLD_MAP_OVERLAY_AREA_IDX];     // 2-5
        //char*     TextureName;                                // 6
        //uint32    TextureWidth;                               // 7
        //uint32    TextureHeight;                              // 8
        //uint32    OffsetX;                                    // 9
        //uint32    OffsetY;                                    // 10
        //uint32    HitRectTop;                                 // 11
        //uint32    HitRectLeft;                                // 12
        //uint32    HitRectBottom;                              // 13
        //uint32    HitRectRight;                               // 14
        //uint32    PlayerConditionID;                          // 15
    };
    
    struct WorldSafeLocsEntry
    {
        uint32          ID;                                     // 0
        uint32          MapID;                                  // 1
        DBCPosition3D   Loc;                                    // 2-4
        float           Facing;                                 // 5 values are in degrees
        //char*         AreaName_lang;                          // 6
    };
    
    struct WorldStateSounds
    {
        uint32    ID;                                           // 0        Worldstate
        uint32    unk;                                          // 1
        uint32    areaTable;                                    // 2
        uint32    WMOAreaTable;                                 // 3
        uint32    zoneIntroMusicTable;                          // 4
        uint32    zoneIntroMusic;                               // 5
        uint32    zoneMusic;                                    // 6
        uint32    soundAmbience;                                // 7
        uint32    soundProviderPreferences;                     // 8
    };
    
    struct WorldStateUI
    {
        uint32    ID;                                           // 0
        uint32    map_id;                                       // 1        Can be -1 to show up everywhere.
        uint32    zone;                                         // 2        Can be zero for "everywhere".
        uint32    phaseMask;                                    // 3        Phase this WorldState is avaliable in
        uint32    icon;                                         // 4        The icon that is used in the interface.
        char*     textureFilename;                              // 5
        char*     text;                                         // 6-21     The worldstate text
        char*     description;                                  // 22-38    Text shown when hovering mouse on icon
        uint32    worldstateID;                                 // 39       This is the actual ID used
        uint32    type;                                         // 40       0 = unknown, 1 = unknown, 2 = not shown in ui, 3 = wintergrasp
        uint32    unk1;                                         // 41
        uint32    unk2;                                         // 43
        uint32    unk3;                                         // 44-58
        uint32    unk4;                                         // 59-61    Used for some progress bars.
        uint32    unk7;                                         // 62       Unused in 3.3.5a
    };
    
From the [Trinity DBC format file](https://github.com/TrinityCore/TrinityCore/blob/6.x/src/server/game/DataStores/DBCfmt.h)

    // x - skip<uint32>, X - skip<uint8>, s - char*, f - float, i - uint32, b - uint8, d - index (not included)
    // n - index (included), l - uint64, p - field present in sql dbc, a - field absent in sql dbc
    
    char const Achievementfmt[] = "niixsxiixixxiii";
    const std::string CustomAchievementfmt = "pppaaaapapaapp";
    const std::string CustomAchievementIndex = "ID";
    char const AreaTableEntryfmt[] = "iiiniixxxxsxixiiiiixxxxxxxxxx";
    char const AreaTriggerEntryfmt[] = "nifffxxxfffffxxxx";
    char const ArmorLocationfmt[] = "nfffff";
    char const AuctionHouseEntryfmt[] = "niiix";
    char const BankBagSlotPricesEntryfmt[] = "ni";
    char const BannedAddOnsfmt[] = "nxxxxxxxxxx";
    char const BarberShopStyleEntryfmt[] = "nixxxiii";
    char const BattlemasterListEntryfmt[] = "niiiiiiiiiiiiiiiiixsiiiixxxxxxx";
    char const CharStartOutfitEntryfmt[] = "dbbbXiiiiiiiiiiiiiiiiiiiiiiiiii";
    char const CharTitlesEntryfmt[] = "nxssix";
    char const ChatChannelsEntryfmt[] = "nixsx";
    char const ChrClassesEntryfmt[] = "nixsxxxixiiiixxxxxx";
    char const ChrRacesEntryfmt[] = "niixiixxxxxxiisxxxxxxxxxxxxxxxxxxxxxxxxx";
    char const ChrClassesXPowerTypesfmt[] = "nii";
    char const ChrSpecializationEntryfmt[] = "nxiiiiiiiiixxxii";
    char const CinematicSequencesEntryfmt[] = "nxxxxxxxxx";
    char const CreatureDisplayInfofmt[] = "nixifxxxxxxxxxxxxxxix";
    char const CreatureDisplayInfoExtrafmt[] = "dixxxxxxxxxxxxxxxxxxxx";
    char const CreatureFamilyfmt[] = "nfifiiiiixsx";
    char const CreatureModelDatafmt[] = "nixxxxxxxxxxxxxffxxxxxxxxxxxxxxxxx";
    char const CreatureSpellDatafmt[] = "niiiixxxx";
    char const CreatureTypefmt[] = "nxx";
    char const Criteriafmt[] = "niiiiiiiixii";
    char const CriteriaTreefmt[] = "niliixxx";
    char const DestructibleModelDatafmt[] = "nixxxixxxxixxxxixxxxxxxx";
    char const DifficultyFmt[] = "niiiixiixxxxi";
    char const DungeonEncounterfmt[] = "niiixsxxx";
    char const DurabilityCostsfmt[] = "niiiiiiiiiiiiiiiiiiiiiiiiiiiii";
    char const DurabilityQualityfmt[] = "nf";
    char const EmotesEntryfmt[] = "nxxiiixx";
    char const EmotesTextEntryfmt[] = "nxixxxxxxxxxxxxxxxx";
    char const FactionEntryfmt[] = "niiiiiiiiiiiiiiiiiiffixsxixx";
    char const FactionTemplateEntryfmt[] = "niiiiiiiiiiiii";
    char const GameObjectDisplayInfofmt[] = "nixxxxxxxxxxffffffxxx";
    char const GameTablesFmt[] = "dsii";
    char const GemPropertiesEntryfmt[] = "nixxii";
    char const GlyphPropertiesfmt[] = "niiix";
    char const GlyphSlotfmt[] = "nix";
    char const GtBarberShopCostBasefmt[] = "xf";
    char const GtCombatRatingsfmt[] = "xf";
    char const GtOCTHpPerStaminafmt[] = "df";
    char const GtOCTLevelExperiencefmt[] = "xf";
    char const GtChanceToMeleeCritBasefmt[] = "xf";
    char const GtChanceToMeleeCritfmt[] = "xf";
    char const GtChanceToSpellCritBasefmt[] = "xf";
    char const GtChanceToSpellCritfmt[] = "xf";
    char const GtItemSocketCostPerLevelfmt[] = "xf";
    char const GtNPCManaCostScalerfmt[] = "xf";
    char const GtNpcTotalHpfmt[] = "xf";
    char const GtNpcTotalHpExp1fmt[] = "xf";
    char const GtNpcTotalHpExp2fmt[] = "xf";
    char const GtNpcTotalHpExp3fmt[] = "xf";
    char const GtNpcTotalHpExp4fmt[] = "xf";
    char const GtNpcTotalHpExp5fmt[] = "xf";
    char const GtOCTRegenHPfmt[] = "f";
    //char const GtOCTRegenMPfmt[] = "f";
    char const GtRegenMPPerSptfmt[] = "xf";
    char const GtSpellScalingfmt[] = "df";
    char const GtOCTBaseHPByClassfmt[] = "df";
    char const GtOCTBaseMPByClassfmt[] = "df";
    char const GuildPerkSpellsfmt[] = "dii";
    char const ImportPriceArmorfmt[] = "nffff";
    char const ImportPriceQualityfmt[] = "nf";
    char const ImportPriceShieldfmt[] = "nf";
    char const ImportPriceWeaponfmt[] = "nf";
    char const ItemPriceBasefmt[] = "diff";
    char const ItemBagFamilyfmt[] = "nx";
    char const ItemArmorQualityfmt[] = "nfffffffi";
    char const ItemArmorShieldfmt[] = "nifffffff";
    char const ItemArmorTotalfmt[] = "niffff";
    char const ItemClassfmt[] = "nxfx";
    char const ItemDamagefmt[] = "nfffffffi";
    char const ItemDisenchantLootfmt[] = "niiiiii";
    //char const ItemDisplayTemplateEntryfmt[] = "nxxxxxxxxxxixxxxxxxxxxx";
    char const ItemLimitCategoryEntryfmt[] = "nxii";
    char const ItemRandomPropertiesfmt[] = "nxiiiiis";
    char const ItemRandomSuffixfmt[] = "nsxiiiiiiiiii";
    char const ItemSetEntryfmt[] = "nsiiiiiiiiiiiiiiiiiii";
    char const ItemSetSpellEntryfmt[] = "niiii";
    char const LFGDungeonEntryfmt[] = "nsiiixxiiiixxixixxxxxxxxxxxxx";
    char const LightEntryfmt[] = "nifffxxxxxxxxxx";
    char const LiquidTypefmt[] = "nxxixixxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    char const LockEntryfmt[] = "niiiiiiiiiiiiiiiiiiiiiiiixxxxxxxx";
    char const PhaseEntryfmt[] = "ni";
    char const MailTemplateEntryfmt[] = "nxs";
    char const MapEntryfmt[] = "nxiixxsixxixiffxiiiixx";
    char const MapDifficultyEntryfmt[] = "diisiiii";
    char const MinorTalentEntryfmt[] = "niii";
    char const MovieEntryfmt[] = "nxxxx";
    char const ModifierTreefmt[] = "niiiiii";
    char const MountCapabilityfmt[] = "niiiiiii";
    char const MountTypefmt[] = "niiiiiiiiiiiiiiiiiiiiiiii";
    char const NameGenfmt[] = "dsii";
    char const NumTalentsAtLevelfmt[] = "df";
    char const QuestFactionRewardfmt[] = "niiiiiiiiii";
    char const QuestSortEntryfmt[] = "nx";
    char const QuestV2fmt[] = "ni";
    char const QuestXPfmt[] = "niiiiiiiiii";
    char const PowerDisplayfmt[] = "nixXXX";
    char const PvPDifficultyfmt[] = "diiii";
    char const RandomPropertiesPointsfmt[] = "niiiiiiiiiiiiiii";
    char const ScalingStatDistributionfmt[] = "niii";
    char const SkillLinefmt[] = "nisxixixx";
    char const SkillLineAbilityfmt[] = "niiiiiiiiiiii";
    char const SkillRaceClassInfofmt[] = "diiiiiii";
    char const SkillTiersfmt[] = "niiiiiiiiiiiiiiii";
    char const SpecializationSpellsEntryfmt[] = "niiix";
    char const SpellCastTimefmt[] = "nixx";
    char const SpellCategoriesEntryfmt[] = "diiiiiiiii";
    char const SpellCategoryfmt[] = "nixxii";
    char const SpellDurationfmt[] = "niii";
    char const SpellEffectEntryfmt[] =            "iiifiiiffiiiiiifiifiiiiifiiiiif";
    const std::string CustomSpellEffectEntryfmt = "ppppppppppppppappppppppppp";
    const std::string CustomSpellEffectEntryIndex = "Id";
    char const SpellEntryfmt[] = "nsxxxiiiiiiiiiiiiiiiiiii";
    const std::string CustomSpellEntryfmt = "ppppppppppppppapaaaaaaaaapaaaaaapapppaapppaaapa";
    const std::string CustomSpellEntryIndex = "Id";
    char const SpellEffectScalingfmt[] = "nfffi";
    char const SpellFocusObjectfmt[] = "nx";
    char const SpellItemEnchantmentfmt[] = "niiiiiiiiiixiiiiiiiiiiifff";
    char const SpellItemEnchantmentConditionfmt[] = "nbbbbbiiiiibbbbbbbbbbiiiiibbbbb";
    char const SpellRadiusfmt[] = "nxfff";
    char const SpellRangefmt[] = "nffffixx";
    char const SpellScalingEntryfmt[] = "niiiifiii";
    char const SpellTargetRestrictionsEntryfmt[] = "niiffiiii";
    char const SpellInterruptsEntryfmt[] = "diiiiiii";
    char const SpellEquippedItemsEntryfmt[] = "diiiii";
    char const SpellAuraOptionsEntryfmt[] = "niiiiiiii";
    char const SpellCooldownsEntryfmt[] = "diiiii";
    char const SpellLevelsEntryfmt[] = "diiiii";
    char const SpellShapeshiftEntryfmt[] = "niiiix";
    char const SpellShapeshiftFormfmt[] = "nxxiixiiiiiiiiiiiiixx";
    char const StableSlotPricesfmt[] = "ni";
    char const SummonPropertiesfmt[] = "niiiii";
    char const TalentEntryfmt[] = "niiiiiiiiix";
    char const TotemCategoryEntryfmt[] = "nxii";
    char const UnitPowerBarfmt[] = "niixxxxxxxxxxxxxxxxxxxxxxxx";
    char const TransportAnimationfmt[] = "diifffx";
    char const TransportRotationfmt[] = "diiffff";
    char const VehicleEntryfmt[] = "niiffffiiiiiiiifffffffffffffffxxxxfifiiii";
    char const VehicleSeatEntryfmt[] = "niiffffffffffiiiiiifffffffiiifffiiiiiiiffiiiiffffffffffffiiiiiiiii";
    char const WMOAreaTableEntryfmt[] = "niiixxxxxiixxxx";
    char const WorldMapAreaEntryfmt[] = "xinxffffixxxxx";
    char const WorldMapOverlayEntryfmt[] = "nxiiiixxxxxxxxxx";
    char const WorldSafeLocsEntryfmt[] = "niffffx";

From the [Trinity DB2 structure file](https://github.com/TrinityCore/TrinityCore/blob/6.x/src/server/game/DataStores/DB2Structure.h)

    #define MAX_BROADCAST_TEXT_EMOTES 3
    #define MAX_HOLIDAY_DURATIONS 10
    #define MAX_HOLIDAY_DATES 16
    #define MAX_HOLIDAY_FLAGS 10
    #define MAX_ITEM_PROTO_FLAGS 3
    #define MAX_ITEM_PROTO_SOCKETS 3
    #define MAX_ITEM_PROTO_STATS  10
    
    struct AreaGroupEntry
    {
        uint32      ID;                                                 // 0
    };
    
    struct AreaGroupMemberEntry
    {
        uint32      ID;                                                 // 0
        uint32      AreaGroupID;                                        // 1
        uint32      AreaID;                                             // 2
    };
    
    struct BroadcastTextEntry
    {
        uint32 ID;
        int32 Language;
        LocalizedString* MaleText;
        LocalizedString* FemaleText;
        uint32 EmoteID[MAX_BROADCAST_TEXT_EMOTES];
        uint32 EmoteDelay[MAX_BROADCAST_TEXT_EMOTES];
        uint32 SoundID;
        uint32 UnkEmoteID;
        uint32 Type;
    };
    
    struct CurrencyTypesEntry
    {
        uint32      ID;                                                 // 0
        uint32      CategoryID;                                         // 1
        LocalizedString* Name_lang;                                     // 2
        LocalizedString* InventoryIcon[2];                              // 3-4
        uint32      SpellWeight;                                        // 5
        uint32      SpellCategory;                                      // 6
        uint32      MaxQty;                                             // 7
        uint32      MaxEarnablePerWeek;                                 // 8
        uint32      Flags;                                              // 9
        uint32      Quality;                                            // 10
        LocalizedString* Description_lang;                              // 11
    };
    
    struct CurvePointEntry
    {
        uint32 ID;                                                      // 0
        uint32 CurveID;                                                 // 1
        uint32 Index;                                                   // 2
        float X;                                                        // 3
        float Y;                                                        // 4
    };
    
    struct HolidaysEntry
    {
        uint32      ID;                                                 // 0
        uint32      Duration[MAX_HOLIDAY_DURATIONS];                    // 1-10
        uint32      Date[MAX_HOLIDAY_DATES];                            // 11-26 (dates in unix time starting at January, 1, 2000)
        uint32      Region;                                             // 27
        uint32      Looping;                                            // 28
        uint32      CalendarFlags[MAX_HOLIDAY_FLAGS];                   // 29-38
        uint32      HolidayNameID;                                      // 39 HolidayNames.dbc
        uint32      HolidayDescriptionID;                               // 40 HolidayDescriptions.dbc
        LocalizedString* TextureFilename;                               // 41
        uint32      Priority;                                           // 42
        uint32      CalendarFilterType;                                 // 43 (-1 = Fishing Contest, 0 = Unk, 1 = Darkmoon Festival, 2 = Yearly holiday)
        uint32      Flags;                                              // 44 (0 = Darkmoon Faire, Fishing Contest and Wotlk Launch, rest is 1)
    };
    
    struct ItemAppearanceEntry
    {
        uint32      ID;                                                 // 0 (reference to ItemModifiedAppearance.db2?)
        uint32      DisplayID;                                          // 1
        uint32      IconFileDataID;                                     // 2
    };
    
    struct ItemBonusEntry
    {
        uint32      ID;                                                 // 0
        uint32      BonusListID;                                        // 1
        uint32      Type;                                               // 2
        int32       Value[2];                                           // 3-4
        uint32      Index;                                              // 5
    };
    
    struct ItemBonusTreeNodeEntry
    {
        uint32      ID;                                                 // 0
        uint32      BonusTreeID;                                        // 1
        uint32      BonusTreeModID;                                     // 2
        uint32      SubTreeID;                                          // 3
        uint32      BonusListID;                                        // 4
    };
    
    struct ItemCurrencyCostEntry
    {
        uint32      ID;                                                 // 0
        uint32      ItemId;                                             // 1
    };
    
    struct ItemEffectEntry
    {
        uint32      ID;                                                 // 0
        uint32      ItemID;                                             // 1
        uint32      OrderIndex;                                         // 2
        uint32      SpellID;                                            // 3
        uint32      Trigger;                                            // 4
        int32       Charges;                                            // 5
        int32       Cooldown;                                           // 6
        uint32      Category;                                           // 7
        int32       CategoryCooldown;                                   // 8
    };
    
    struct ItemEntry
    {
        uint32      ID;                                                 // 0
        uint32      Class;                                              // 1
        uint32      SubClass;                                           // 2
        int32       SoundOverrideSubclass;                              // 3
        int32       Material;                                           // 4
        uint32      InventoryType;                                      // 5
        uint32      Sheath;                                             // 6
        uint32      FileDataID;                                         // 7
        uint32      GroupSoundsID;                                      // 8
    };
    
    #define MAX_ITEM_EXT_COST_ITEMS         5
    #define MAX_ITEM_EXT_COST_CURRENCIES    5
    
    struct ItemExtendedCostEntry
    {
        uint32      ID;                                                 // 0 extended-cost entry id
        uint32      RequiredArenaSlot;                                  // 3 arena slot restrictions (min slot value)
        uint32      RequiredItem[MAX_ITEM_EXT_COST_ITEMS];              // 3-6 required item id
        uint32      RequiredItemCount[MAX_ITEM_EXT_COST_ITEMS];         // 7-11 required count of 1st item
        uint32      RequiredPersonalArenaRating;                        // 12 required personal arena rating
        uint32      ItemPurchaseGroup;                                  // 13
        uint32      RequiredCurrency[MAX_ITEM_EXT_COST_CURRENCIES];     // 14-18 required curency id
        uint32      RequiredCurrencyCount[MAX_ITEM_EXT_COST_CURRENCIES];// 19-23 required curency count
        uint32      RequiredFactionId;                                  // 24
        uint32      RequiredFactionStanding;                            // 25
        uint32      RequirementFlags;                                   // 26
        uint32      RequiredAchievement;                                // 27
        uint32      RequiredMoney;                                      // 28
    };
    
    struct ItemModifiedAppearanceEntry
    {
        uint32      ID;                                                 // 0
        uint32      ItemID;                                             // 1
        uint32      AppearanceModID;                                    // 2
        uint32      AppearanceID;                                       // 3
        uint32      IconFileDataID;                                     // 4
        uint32      Index;                                              // 5
    };
    
    struct ItemSparseEntry
    {
        uint32      ID;                                                 // 0
        uint32      Quality;                                            // 1
        uint32      Flags[MAX_ITEM_PROTO_FLAGS];                        // 2-4
        float       Unk1;                                               // 5
        float       Unk2;                                               // 6
        uint32      BuyCount;                                           // 7
        uint32      BuyPrice;                                           // 8
        uint32      SellPrice;                                          // 9
        uint32      InventoryType;                                      // 10
        int32       AllowableClass;                                     // 11
        int32       AllowableRace;                                      // 12
        uint32      ItemLevel;                                          // 13
        int32       RequiredLevel;                                      // 14
        uint32      RequiredSkill;                                      // 15
        uint32      RequiredSkillRank;                                  // 16
        uint32      RequiredSpell;                                      // 17
        uint32      RequiredHonorRank;                                  // 18
        uint32      RequiredCityRank;                                   // 19
        uint32      RequiredReputationFaction;                          // 20
        uint32      RequiredReputationRank;                             // 21
        uint32      MaxCount;                                           // 22
        uint32      Stackable;                                          // 23
        uint32      ContainerSlots;                                     // 24
        int32       ItemStatType[MAX_ITEM_PROTO_STATS];                 // 25 - 34
        int32       ItemStatValue[MAX_ITEM_PROTO_STATS];                // 35 - 44
        int32       ItemStatAllocation[MAX_ITEM_PROTO_STATS];           // 45 - 54
        float       ItemStatSocketCostMultiplier[MAX_ITEM_PROTO_STATS]; // 55 - 64
        uint32      ScalingStatDistribution;                            // 65
        uint32      DamageType;                                         // 66
        uint32      Delay;                                              // 67
        float       RangedModRange;                                     // 68
        uint32      Bonding;                                            // 69
        LocalizedString* Name;                                          // 70
        LocalizedString* Name2;                                         // 71
        LocalizedString* Name3;                                         // 72
        LocalizedString* Name4;                                         // 73
        LocalizedString* Description;                                   // 74
        uint32      PageText;                                           // 75
        uint32      LanguageID;                                         // 76
        uint32      PageMaterial;                                       // 77
        uint32      StartQuest;                                         // 78
        uint32      LockID;                                             // 79
        int32       Material;                                           // 80
        uint32      Sheath;                                             // 81
        uint32      RandomProperty;                                     // 82
        uint32      RandomSuffix;                                       // 83
        uint32      ItemSet;                                            // 84
        uint32      Area;                                               // 85
        uint32      Map;                                                // 86
        uint32      BagFamily;                                          // 87
        uint32      TotemCategory;                                      // 88
        uint32      SocketColor[MAX_ITEM_PROTO_SOCKETS];                // 89-91
        uint32      SocketBonus;                                        // 92
        uint32      GemProperties;                                      // 93
        float       ArmorDamageModifier;                                // 94
        uint32      Duration;                                           // 95
        uint32      ItemLimitCategory;                                  // 96
        uint32      HolidayID;                                          // 97
        float       StatScalingFactor;                                  // 98
        uint32      CurrencySubstitutionID;                             // 99
        uint32      CurrencySubstitutionCount;                          // 100
        uint32      ItemNameDescriptionID;                              // 101
    };
    
    struct ItemXBonusTreeEntry
    {
        uint32      ID;                                                 // 0
        uint32      ItemID;                                             // 1
        uint32      BonusTreeID;                                        // 2
    };
    
    #define KEYCHAIN_SIZE   32
    
    struct KeyChainEntry
    {
        uint32      Id;
        uint8       Key[KEYCHAIN_SIZE];
    };
    
    struct MountEntry
    {
        uint32 Id;
        uint32 MountTypeId;
        uint32 DisplayId;
        uint32 Flags;
        LocalizedString* Name;
        LocalizedString* Description;
        LocalizedString* SourceDescription;
        uint32 Source;
        uint32 SpellId;
        uint32 PlayerConditionId;
    };
    
    #define MAX_OVERRIDE_SPELL 10
    
    struct OverrideSpellDataEntry
    {
        uint32      ID;                                                 // 0
        uint32      SpellID[MAX_OVERRIDE_SPELL];                        // 1-10
        uint32      Flags;                                              // 11
        uint32      PlayerActionbarFileDataID;                          // 12
    };
    
    struct PhaseXPhaseGroupEntry
    {
        uint32      ID;
        uint32      PhaseID;
        uint32      PhaseGroupID;
    };
    
    struct SoundEntriesEntry
    {
        uint32      ID;                                                 // 0
        uint32      SoundType;                                          // 1
        LocalizedString* Name;                                          // 2
        uint32      FileDataID[20];                                     // 3-22
        uint32      Freq[20];                                           // 23-42
        float       VolumeFloat;                                        // 43
        uint32      Flags;                                              // 44
        float       MinDistance;                                        // 45
        float       DistanceCutoff;                                     // 46
        uint32      EAXDef;                                             // 47
        uint32      SoundEntriesAdvancedID;                             // 48
        float       VolumeVariationPlus;                                // 49
        float       VolumeVariationMinus;                               // 50
        float       PitchVariationPlus;                                 // 51
        float       PitchVariationMinus;                                // 52
        float       PitchAdjust;                                        // 53
        uint32      DialogType;                                         // 54
        uint32      BusOverwriteID;                                     // 55
    };
    
    struct SpellAuraRestrictionsEntry
    {
        uint32      ID;                                                 // 0
        uint32      CasterAuraState;                                    // 1
        uint32      TargetAuraState;                                    // 2
        uint32      ExcludeCasterAuraState;                             // 3
        uint32      ExcludeTargetAuraState;                             // 4
        uint32      CasterAuraSpell;                                    // 5
        uint32      TargetAuraSpell;                                    // 6
        uint32      ExcludeCasterAuraSpell;                             // 7
        uint32      ExcludeTargetAuraSpell;                             // 8
    };
    
    struct SpellCastingRequirementsEntry
    {
        uint32      ID;                                                 // 0
        uint32      FacingCasterFlags;                                  // 1
        uint32      MinFactionID;                                       // 1
        uint32      MinReputation;                                      // 3
        uint32      RequiredAreasID;                                    // 4
        uint32      RequiredAuraVision;                                 // 5
        uint32      RequiresSpellFocus;                                 // 6
    };
    
    struct SpellClassOptionsEntry
    {
        uint32      ID;                                                 // 0
        uint32      ModalNextSpell;                                     // 1
        flag128     SpellClassMask;                                     // 2
        uint32      SpellClassSet;                                      // 3
    };
    
    struct SpellLearnSpellEntry
    {
        uint32      ID;                                                 // 0
        uint32      LearnSpellID;                                       // 1
        uint32      SpellID;                                            // 2
        uint32      OverridesSpellID;                                   // 3
    };
    
    struct SpellMiscEntry
    {
        uint32      ID;                                                 // 0
        uint32      Attributes;                                         // 1
        uint32      AttributesEx;                                       // 2
        uint32      AttributesExB;                                      // 3
        uint32      AttributesExC;                                      // 4
        uint32      AttributesExD;                                      // 5
        uint32      AttributesExE;                                      // 6
        uint32      AttributesExF;                                      // 7
        uint32      AttributesExG;                                      // 8
        uint32      AttributesExH;                                      // 9
        uint32      AttributesExI;                                      // 10
        uint32      AttributesExJ;                                      // 11
        uint32      AttributesExK;                                      // 12
        uint32      AttributesExL;                                      // 13
        uint32      AttributesExM;                                      // 14
        uint32      CastingTimeIndex;                                   // 15
        uint32      DurationIndex;                                      // 16
        uint32      RangeIndex;                                         // 17
        float       Speed;                                              // 18
        uint32      SpellVisualID[2];                                   // 19-20
        uint32      SpellIconID;                                        // 21
        uint32      ActiveIconID;                                       // 22
        uint32      SchoolMask;                                         // 23
        float       MultistrikeSpeedMod;                                // 24
    };
    
    struct SpellPowerEntry
    {
        uint32      ID;                                                 // 0
        uint32      SpellID;                                            // 1
        uint32      PowerIndex;                                         // 2
        uint32      PowerType;                                          // 3
        uint32      ManaCost;                                           // 4
        uint32      ManaCostPerLevel;                                   // 5
        uint32      ManaCostPerSecond;                                  // 6
        uint32      ManaCostAdditional;                                 // 7 Spell uses [ManaCost, ManaCost+ManaCostAdditional] power - affects tooltip parsing as multiplier on SpellEffectEntry::EffectPointsPerResource
                                                                        //   only SPELL_EFFECT_WEAPON_DAMAGE_NOSCHOOL, SPELL_EFFECT_WEAPON_PERCENT_DAMAGE, SPELL_EFFECT_WEAPON_DAMAGE, SPELL_EFFECT_NORMALIZED_WEAPON_DMG
        uint32      PowerDisplayID;                                     // 8
        uint32      UnitPowerBarID;                                     // 9
        float       ManaCostPercentage;                                 // 10
        float       ManaCostPercentagePerSecond;                        // 11
        uint32      RequiredAura;                                       // 12
        float       HealthCostPercentage;                               // 13
    };
    
    struct SpellPowerDifficultyEntry
    {
        uint32      SpellPowerID;                                       // 0
        uint32      DifficultyID;                                       // 1
        uint32      PowerIndex;                                         // 2
    };
    
    #define MAX_SPELL_REAGENTS 8
    
    struct SpellReagentsEntry
    {
        uint32      ID;                                                 // 0
        int32       Reagent[MAX_SPELL_REAGENTS];                        // 1-8
        uint32      ReagentCount[MAX_SPELL_REAGENTS];                   // 9-16
        uint32      CurrencyID;                                         // 17
        uint32      CurrencyCount;                                      // 18
    };
    
    struct SpellRuneCostEntry
    {
        uint32      ID;                                                 // 0
        uint32      RuneCost[4];                                        // 1-4 (0=blood, 1=unholy, 2=frost, 3=death)
        uint32      RunicPower;                                         // 5
    
        bool NoRuneCost() const { return RuneCost[0] == 0 && RuneCost[1] == 0 && RuneCost[2] == 0 && RuneCost[3] == 0; }
        bool NoRunicPowerGain() const { return RunicPower == 0; }
    };
    
    #define MAX_SPELL_TOTEMS 2
    
    struct SpellTotemsEntry
    {
        uint32      ID;                                                 // 0
        uint32      RequiredTotemCategoryID[MAX_SPELL_TOTEMS];          // 1
        uint32      Totem[MAX_SPELL_TOTEMS];                            // 2
    };
    
    struct TaxiNodesEntry
    {
        uint32           ID;                                            // 0
        uint32           MapID;                                         // 1
        DBCPosition3D    Pos;                                           // 2-4
        LocalizedString* Name_lang;                                     // 5
        uint32           MountCreatureID[2];                            // 6-7
        uint32           ConditionID;                                   // 8
        uint32           LearnableIndex;                                // 9 - some kind of index only for learnable nodes
        uint32           Flags;                                         // 10
        DBCPosition2D    MapOffset;                                     // 11-12
    };
    
    struct TaxiPathEntry
    {
        uint32          ID;                                             // 0
        uint32          From;                                           // 1
        uint32          To;                                             // 2
        uint32          Cost;                                           // 3
    };
    
    struct TaxiPathNodeEntry
    {
        uint32          ID;                                             // 0
        uint32          PathID;                                         // 1
        uint32          NodeIndex;                                      // 2
        uint32          MapID;                                          // 3
        DBCPosition3D   Loc;                                            // 4-6
        uint32          Flags;                                          // 7
        uint32          Delay;                                          // 8
        uint32          ArrivalEventID;                                 // 9
        uint32          DepartureEventID;                               // 10
    };

From the [Trinity DB2 format file](https://github.com/TrinityCore/TrinityCore/blob/6.x/src/server/game/DataStores/DB2fmt.h)

    char const AreaGroupFormat[] = "n";
    char const AreaGroupMemberFormat[] = "nii";
    char const BroadcastTextFormat[] = "nissiiiiiiiii";
    char const CurrencyTypesFormat[] = "nisssiiiiiis";
    char const CurvePointFormat[] = "niiff";
    char const HolidaysEntryFormat[] = "niiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiisiii";
    char const ItemFormat[] = "niiiiiiii";
    char const ItemAppearanceFormat[] = "nii";
    char const ItemBonusFormat[] = "niiiii";
    char const ItemBonusTreeNodeFormat[] = "niiii";
    char const ItemCurrencyCostFormat[] = "in";
    char const ItemExtendedCostFormat[] = "niiiiiiiiiiiiiiiiiiiiiiiiiiii";
    char const ItemEffectFormat[] = "niiiiiiii";
    char const ItemModifiedAppearanceFormat[] = "niiiii";
    char const ItemSparseFormat[] = "niiiiffiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiffffffffffiiifisssssiiiiiiiiiiiiiiiiiiifiiifiii";
    char const ItemXBonusTreeFormat[] = "nii";
    char const KeyChainFormat[] = "nbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
    char const MountFormat[] = "niiisssiii";
    char const OverrideSpellDataFormat[] = "niiiiiiiiiiii";
    char const PhaseXPhaseGroupFormat[] = "nii";
    char const SoundEntriesFormat[] = "nisiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiififfiifffffii";
    char const SpellAuraRestrictionsFormat[] = "niiiiiiii";
    char const SpellCastingRequirementsFormat[] = "niiiiii";
    char const SpellClassOptionsFormat[] = "niiiiii";
    char const SpellLearnSpellFormat[] = "niii";
    char const SpellMiscFormat[] = "niiiiiiiiiiiiiiiiifiiiiif";
    char const SpellPowerFormat[] = "niiiiiiiiiffif";
    char const SpellPowerDifficultyFormat[] = "nii";
    char const SpellReagentsFormat[] = "niiiiiiiiiiiiiiiiii";
    char const SpellRuneCostFormat[] = "niiiii";
    char const SpellTotemsFormat[] = "niiii";
    char const TaxiNodesFormat[] = "nifffsiiiiiff";
    char const TaxiPathFormat[] = "niii";
    char const TaxiPathNodeFormat[] = "niiifffiiii";

From the [Trinity datastore enums](https://github.com/TrinityCore/TrinityCore/blob/6.x/src/server/game/DataStores/DBCEnums.h)

    struct DBCPosition2D
    {
        float X;
        float Y;
    };
    
    struct DBCPosition3D
    {
        float X;
        float Y;
        float Z;
    };
    
    enum LevelLimit
    {
        // Client expected level limitation, like as used in DBC item max levels for "until max player level"
        // use as default max player level, must be fit max level for used client
        // also see MAX_LEVEL and STRONG_MAX_LEVEL define
        DEFAULT_MAX_LEVEL = 100,
    
        // client supported max level for player/pets/etc. Avoid overflow or client stability affected.
        // also see GT_MAX_LEVEL define
        MAX_LEVEL = 100,
    
        // Server side limitation. Base at used code requirements.
        // also see MAX_LEVEL and GT_MAX_LEVEL define
        STRONG_MAX_LEVEL = 255,
    };
    
    enum BattlegroundBracketId                                  // bracketId for level ranges
    {
        BG_BRACKET_ID_FIRST          = 0,
        BG_BRACKET_ID_LAST           = 10,
    
        // must be max value in PvPDificulty slot + 1
        MAX_BATTLEGROUND_BRACKETS
    };
    
    enum AreaTeams
    {
        AREATEAM_NONE  = 0,
        AREATEAM_ALLY  = 2,
        AREATEAM_HORDE = 4,
        AREATEAM_ANY   = AREATEAM_ALLY+AREATEAM_HORDE
    };
    
    enum AchievementFaction
    {
        ACHIEVEMENT_FACTION_HORDE           = 0,
        ACHIEVEMENT_FACTION_ALLIANCE        = 1,
        ACHIEVEMENT_FACTION_ANY             = -1
    };
    
    enum AchievementFlags
    {
        ACHIEVEMENT_FLAG_COUNTER               = 0x00000001,    // Just count statistic (never stop and complete)
        ACHIEVEMENT_FLAG_HIDDEN                = 0x00000002,    // Not sent to client - internal use only
        ACHIEVEMENT_FLAG_PLAY_NO_VISUAL        = 0x00000004,    // Client does not play achievement earned visual
        ACHIEVEMENT_FLAG_SUMM                  = 0x00000008,    // Use summ criteria value from all requirements (and calculate max value)
        ACHIEVEMENT_FLAG_MAX_USED              = 0x00000010,    // Show max criteria (and calculate max value ??)
        ACHIEVEMENT_FLAG_REQ_COUNT             = 0x00000020,    // Use not zero req count (and calculate max value)
        ACHIEVEMENT_FLAG_AVERAGE               = 0x00000040,    // Show as average value (value / time_in_days) depend from other flag (by def use last criteria value)
        ACHIEVEMENT_FLAG_BAR                   = 0x00000080,    // Show as progress bar (value / max vale) depend from other flag (by def use last criteria value)
        ACHIEVEMENT_FLAG_REALM_FIRST_REACH     = 0x00000100,    //
        ACHIEVEMENT_FLAG_REALM_FIRST_KILL      = 0x00000200,    //
        ACHIEVEMENT_FLAG_UNK3                  = 0x00000400,    // ACHIEVEMENT_FLAG_HIDE_NAME_IN_TIE
        ACHIEVEMENT_FLAG_UNK4                  = 0x00000800,    // first guild on realm done something
        ACHIEVEMENT_FLAG_SHOW_IN_GUILD_NEWS    = 0x00001000,    // Shows in guild news
        ACHIEVEMENT_FLAG_SHOW_IN_GUILD_HEADER  = 0x00002000,    // Shows in guild news header
        ACHIEVEMENT_FLAG_GUILD                 = 0x00004000,    //
        ACHIEVEMENT_FLAG_SHOW_GUILD_MEMBERS    = 0x00008000,    //
        ACHIEVEMENT_FLAG_SHOW_CRITERIA_MEMBERS = 0x00010000,    //
        ACHIEVEMENT_FLAG_ACCOUNT               = 0x00020000
    };
    
    enum AchievementCriteriaCondition
    {
        ACHIEVEMENT_CRITERIA_CONDITION_NONE            = 0,
        ACHIEVEMENT_CRITERIA_CONDITION_NO_DEATH        = 1,    // reset progress on death
        ACHIEVEMENT_CRITERIA_CONDITION_UNK2            = 2,    // only used in "Complete a daily quest every day for five consecutive days"
        ACHIEVEMENT_CRITERIA_CONDITION_BG_MAP          = 3,    // requires you to be on specific map, reset at change
        ACHIEVEMENT_CRITERIA_CONDITION_NO_LOSE         = 4,    // only used in "Win 10 arenas without losing"
        ACHIEVEMENT_CRITERIA_CONDITION_UNK5            = 5,    // Have spell?
        ACHIEVEMENT_CRITERIA_CONDITION_UNK8            = 8,
        ACHIEVEMENT_CRITERIA_CONDITION_NO_SPELL_HIT    = 9,    // requires the player not to be hit by specific spell
        ACHIEVEMENT_CRITERIA_CONDITION_NOT_IN_GROUP    = 10,   // requires the player not to be in group
        ACHIEVEMENT_CRITERIA_CONDITION_UNK13           = 13    // unk
    };
    
    enum AchievementCriteriaAdditionalCondition
    {
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_SOURCE_DRUNK_VALUE          = 1, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_UNK2                        = 2,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_ITEM_LEVEL                  = 3, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_CREATURE_ENTRY       = 4,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_MUST_BE_PLAYER       = 5,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_MUST_BE_DEAD         = 6,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_MUST_BE_ENEMY        = 7,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_SOURCE_HAS_AURA             = 8,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_HAS_AURA             = 10,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_HAS_AURA_TYPE        = 11,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_ITEM_QUALITY_MIN            = 14,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_ITEM_QUALITY_EQUALS         = 15,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_UNK16                       = 16,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_SOURCE_AREA_OR_ZONE         = 17,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_AREA_OR_ZONE         = 18,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_MAP_DIFFICULTY              = 20,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_CREATURE_YIELDS_XP   = 21, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_ARENA_TYPE                  = 24,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_SOURCE_RACE                 = 25,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_SOURCE_CLASS                = 26,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_RACE                 = 27,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_CLASS                = 28,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_MAX_GROUP_MEMBERS           = 29,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_CREATURE_TYPE        = 30,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_SOURCE_MAP                  = 32,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_ITEM_CLASS                  = 33, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_ITEM_SUBCLASS               = 34, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_COMPLETE_QUEST_NOT_IN_GROUP = 35, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_MIN_PERSONAL_RATING         = 37, // NYI (when implementing don't forget about ACHIEVEMENT_CRITERIA_CONDITION_NO_LOSE)
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TITLE_BIT_INDEX             = 38,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_SOURCE_LEVEL                = 39,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_LEVEL                = 40,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_ZONE                 = 41,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_TARGET_HEALTH_PERCENT_BELOW = 46,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_UNK55                       = 55,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_MIN_ACHIEVEMENT_POINTS      = 56, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_REQUIRES_LFG_GROUP          = 58, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_UNK60                       = 60,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_REQUIRES_GUILD_GROUP        = 61, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_GUILD_REPUTATION            = 62, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_RATED_BATTLEGROUND          = 63, // NYI
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_PROJECT_RARITY              = 65,
        ACHIEVEMENT_CRITERIA_ADDITIONAL_CONDITION_PROJECT_RACE                = 66,
    };
    
    enum AchievementCriteriaFlags
    {
        ACHIEVEMENT_CRITERIA_FLAG_SHOW_PROGRESS_BAR = 0x00000001,         // Show progress as bar
        ACHIEVEMENT_CRITERIA_FLAG_HIDDEN            = 0x00000002,         // Not show criteria in client
        ACHIEVEMENT_CRITERIA_FLAG_FAIL_ACHIEVEMENT  = 0x00000004,         // BG related??
        ACHIEVEMENT_CRITERIA_FLAG_RESET_ON_START    = 0x00000008,         //
        ACHIEVEMENT_CRITERIA_FLAG_IS_DATE           = 0x00000010,         // not used
        ACHIEVEMENT_CRITERIA_FLAG_MONEY_COUNTER     = 0x00000020          // Displays counter as money
    };
    
    enum AchievementCriteriaTimedTypes
    {
        ACHIEVEMENT_TIMED_TYPE_EVENT            = 1,    // Timer is started by internal event with id in timerStartEvent
        ACHIEVEMENT_TIMED_TYPE_QUEST            = 2,    // Timer is started by accepting quest with entry in timerStartEvent
        ACHIEVEMENT_TIMED_TYPE_SPELL_CASTER     = 5,    // Timer is started by casting a spell with entry in timerStartEvent
        ACHIEVEMENT_TIMED_TYPE_SPELL_TARGET     = 6,    // Timer is started by being target of spell with entry in timerStartEvent
        ACHIEVEMENT_TIMED_TYPE_CREATURE         = 7,    // Timer is started by killing creature with entry in timerStartEvent
        ACHIEVEMENT_TIMED_TYPE_ITEM             = 9,    // Timer is started by using item with entry in timerStartEvent
        ACHIEVEMENT_TIMED_TYPE_UNK              = 10,   // Unknown
        ACHIEVEMENT_TIMED_TYPE_UNK_2            = 13,   // Unknown
        ACHIEVEMENT_TIMED_TYPE_SCENARIO_STAGE   = 14,   // Timer is started by changing stages in a scenario
    
        ACHIEVEMENT_TIMED_TYPE_MAX
    };
    
    enum AchievementCriteriaTypes
    {
        ACHIEVEMENT_CRITERIA_TYPE_KILL_CREATURE                 = 0,
        ACHIEVEMENT_CRITERIA_TYPE_WIN_BG                        = 1,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_ARCHAEOLOGY_PROJECTS = 3, // struct { uint32 itemCount; }
        ACHIEVEMENT_CRITERIA_TYPE_REACH_LEVEL                   = 5,
        ACHIEVEMENT_CRITERIA_TYPE_REACH_SKILL_LEVEL             = 7,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_ACHIEVEMENT          = 8,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUEST_COUNT          = 9,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_DAILY_QUEST_DAILY    = 10, // you have to complete a daily quest x times in a row
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUESTS_IN_ZONE       = 11,
        ACHIEVEMENT_CRITERIA_TYPE_CURRENCY                      = 12,
        ACHIEVEMENT_CRITERIA_TYPE_DAMAGE_DONE                   = 13,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_DAILY_QUEST          = 14,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_BATTLEGROUND         = 15,
        ACHIEVEMENT_CRITERIA_TYPE_DEATH_AT_MAP                  = 16,
        ACHIEVEMENT_CRITERIA_TYPE_DEATH                         = 17,
        ACHIEVEMENT_CRITERIA_TYPE_DEATH_IN_DUNGEON              = 18,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_RAID                 = 19,
        ACHIEVEMENT_CRITERIA_TYPE_KILLED_BY_CREATURE            = 20,
        ACHIEVEMENT_CRITERIA_TYPE_KILLED_BY_PLAYER              = 23,
        ACHIEVEMENT_CRITERIA_TYPE_FALL_WITHOUT_DYING            = 24,
        ACHIEVEMENT_CRITERIA_TYPE_DEATHS_FROM                   = 26,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUEST                = 27,
        ACHIEVEMENT_CRITERIA_TYPE_BE_SPELL_TARGET               = 28,
        ACHIEVEMENT_CRITERIA_TYPE_CAST_SPELL                    = 29,
        ACHIEVEMENT_CRITERIA_TYPE_BG_OBJECTIVE_CAPTURE          = 30,
        ACHIEVEMENT_CRITERIA_TYPE_HONORABLE_KILL_AT_AREA        = 31,
        ACHIEVEMENT_CRITERIA_TYPE_WIN_ARENA                     = 32,
        ACHIEVEMENT_CRITERIA_TYPE_PLAY_ARENA                    = 33,
        ACHIEVEMENT_CRITERIA_TYPE_LEARN_SPELL                   = 34,
        ACHIEVEMENT_CRITERIA_TYPE_HONORABLE_KILL                = 35,
        ACHIEVEMENT_CRITERIA_TYPE_OWN_ITEM                      = 36,
        ACHIEVEMENT_CRITERIA_TYPE_WIN_RATED_ARENA               = 37,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_TEAM_RATING           = 38,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_PERSONAL_RATING       = 39,
        ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILL_LEVEL             = 40,
        ACHIEVEMENT_CRITERIA_TYPE_USE_ITEM                      = 41,
        ACHIEVEMENT_CRITERIA_TYPE_LOOT_ITEM                     = 42,
        ACHIEVEMENT_CRITERIA_TYPE_EXPLORE_AREA                  = 43,
        ACHIEVEMENT_CRITERIA_TYPE_OWN_RANK                      = 44,
        ACHIEVEMENT_CRITERIA_TYPE_BUY_BANK_SLOT                 = 45,
        ACHIEVEMENT_CRITERIA_TYPE_GAIN_REPUTATION               = 46,
        ACHIEVEMENT_CRITERIA_TYPE_GAIN_EXALTED_REPUTATION       = 47,
        ACHIEVEMENT_CRITERIA_TYPE_VISIT_BARBER_SHOP             = 48,
        ACHIEVEMENT_CRITERIA_TYPE_EQUIP_EPIC_ITEM               = 49,
        ACHIEVEMENT_CRITERIA_TYPE_ROLL_NEED_ON_LOOT             = 50, /// @todo itemlevel is mentioned in text but not present in dbc
        ACHIEVEMENT_CRITERIA_TYPE_ROLL_GREED_ON_LOOT            = 51,
        ACHIEVEMENT_CRITERIA_TYPE_HK_CLASS                      = 52,
        ACHIEVEMENT_CRITERIA_TYPE_HK_RACE                       = 53,
        ACHIEVEMENT_CRITERIA_TYPE_DO_EMOTE                      = 54,
        ACHIEVEMENT_CRITERIA_TYPE_HEALING_DONE                  = 55,
        ACHIEVEMENT_CRITERIA_TYPE_GET_KILLING_BLOWS             = 56, /// @todo in some cases map not present, and in some cases need do without die
        ACHIEVEMENT_CRITERIA_TYPE_EQUIP_ITEM                    = 57,
        ACHIEVEMENT_CRITERIA_TYPE_MONEY_FROM_VENDORS            = 59,
        ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_FOR_TALENTS        = 60,
        ACHIEVEMENT_CRITERIA_TYPE_NUMBER_OF_TALENT_RESETS       = 61,
        ACHIEVEMENT_CRITERIA_TYPE_MONEY_FROM_QUEST_REWARD       = 62,
        ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_FOR_TRAVELLING     = 63,
        ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_AT_BARBER          = 65,
        ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_FOR_MAIL           = 66,
        ACHIEVEMENT_CRITERIA_TYPE_LOOT_MONEY                    = 67,
        ACHIEVEMENT_CRITERIA_TYPE_USE_GAMEOBJECT                = 68,
        ACHIEVEMENT_CRITERIA_TYPE_BE_SPELL_TARGET2              = 69,
        ACHIEVEMENT_CRITERIA_TYPE_SPECIAL_PVP_KILL              = 70,
        ACHIEVEMENT_CRITERIA_TYPE_FISH_IN_GAMEOBJECT            = 72,
        /// @todo 73: Achievements 1515, 1241, 1103 (Name: Mal'Ganis)
        ACHIEVEMENT_CRITERIA_TYPE_ON_LOGIN                      = 74,
        ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILLLINE_SPELLS        = 75,
        ACHIEVEMENT_CRITERIA_TYPE_WIN_DUEL                      = 76,
        ACHIEVEMENT_CRITERIA_TYPE_LOSE_DUEL                     = 77,
        ACHIEVEMENT_CRITERIA_TYPE_KILL_CREATURE_TYPE            = 78,
        ACHIEVEMENT_CRITERIA_TYPE_GOLD_EARNED_BY_AUCTIONS       = 80,
        ACHIEVEMENT_CRITERIA_TYPE_CREATE_AUCTION                = 82,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_AUCTION_BID           = 83,
        ACHIEVEMENT_CRITERIA_TYPE_WON_AUCTIONS                  = 84,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_AUCTION_SOLD          = 85,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_GOLD_VALUE_OWNED      = 86,
        ACHIEVEMENT_CRITERIA_TYPE_GAIN_REVERED_REPUTATION       = 87,
        ACHIEVEMENT_CRITERIA_TYPE_GAIN_HONORED_REPUTATION       = 88,
        ACHIEVEMENT_CRITERIA_TYPE_KNOWN_FACTIONS                = 89,
        ACHIEVEMENT_CRITERIA_TYPE_LOOT_EPIC_ITEM                = 90,
        ACHIEVEMENT_CRITERIA_TYPE_RECEIVE_EPIC_ITEM             = 91,
        ACHIEVEMENT_CRITERIA_TYPE_ROLL_NEED                     = 93,
        ACHIEVEMENT_CRITERIA_TYPE_ROLL_GREED                    = 94,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HIT_DEALT             = 101,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HIT_RECEIVED          = 102,
        ACHIEVEMENT_CRITERIA_TYPE_TOTAL_DAMAGE_RECEIVED         = 103,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HEAL_CAST             = 104,
        ACHIEVEMENT_CRITERIA_TYPE_TOTAL_HEALING_RECEIVED        = 105,
        ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HEALING_RECEIVED      = 106,
        ACHIEVEMENT_CRITERIA_TYPE_QUEST_ABANDONED               = 107,
        ACHIEVEMENT_CRITERIA_TYPE_FLIGHT_PATHS_TAKEN            = 108,
        ACHIEVEMENT_CRITERIA_TYPE_LOOT_TYPE                     = 109,
        ACHIEVEMENT_CRITERIA_TYPE_CAST_SPELL2                   = 110, /// @todo target entry is missing
        ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILL_LINE              = 112,
        ACHIEVEMENT_CRITERIA_TYPE_EARN_HONORABLE_KILL           = 113,
        ACHIEVEMENT_CRITERIA_TYPE_ACCEPTED_SUMMONINGS           = 114,
        ACHIEVEMENT_CRITERIA_TYPE_EARN_ACHIEVEMENT_POINTS       = 115,
        ACHIEVEMENT_CRITERIA_TYPE_USE_LFD_TO_GROUP_WITH_PLAYERS = 119,
        ACHIEVEMENT_CRITERIA_TYPE_SPENT_GOLD_GUILD_REPAIRS      = 124,
        ACHIEVEMENT_CRITERIA_TYPE_REACH_GUILD_LEVEL             = 125,
        ACHIEVEMENT_CRITERIA_TYPE_CRAFT_ITEMS_GUILD             = 126,
        ACHIEVEMENT_CRITERIA_TYPE_CATCH_FROM_POOL               = 127,
        ACHIEVEMENT_CRITERIA_TYPE_BUY_GUILD_BANK_SLOTS          = 128,
        ACHIEVEMENT_CRITERIA_TYPE_EARN_GUILD_ACHIEVEMENT_POINTS = 129,
        ACHIEVEMENT_CRITERIA_TYPE_WIN_RATED_BATTLEGROUND        = 130,
        ACHIEVEMENT_CRITERIA_TYPE_REACH_BG_RATING               = 132,
        ACHIEVEMENT_CRITERIA_TYPE_BUY_GUILD_TABARD              = 133,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUESTS_GUILD         = 134,
        ACHIEVEMENT_CRITERIA_TYPE_HONORABLE_KILLS_GUILD         = 135,
        ACHIEVEMENT_CRITERIA_TYPE_KILL_CREATURE_TYPE_GUILD      = 136,
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_GUILD_CHALLENGE_TYPE = 138, //struct { Flag flag; uint32 count; } 1: Guild Dungeon, 2:Guild Challenge, 3:Guild battlefield
        ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_GUILD_CHALLENGE      = 139  //struct { uint32 count; } Guild Challenge
    };
    
    #define ACHIEVEMENT_CRITERIA_TYPE_TOTAL 188
    
    enum AchievementCriteriaTreeOperator
    {
        ACHIEVEMENT_CRITERIA_TREE_OPERATOR_ALL  = 4,
        ACHIEVEMENT_CRITERIA_TREE_OPERATOR_ANY  = 8
    };
    
    enum AreaFlags
    {
        AREA_FLAG_SNOW                  = 0x00000001,                // snow (only Dun Morogh, Naxxramas, Razorfen Downs and Winterspring)
        AREA_FLAG_UNK1                  = 0x00000002,                // Razorfen Downs, Naxxramas and Acherus: The Ebon Hold (3.3.5a)
        AREA_FLAG_UNK2                  = 0x00000004,                // Only used for areas on map 571 (development before)
        AREA_FLAG_SLAVE_CAPITAL         = 0x00000008,                // city and city subsones
        AREA_FLAG_UNK3                  = 0x00000010,                // can't find common meaning
        AREA_FLAG_SLAVE_CAPITAL2        = 0x00000020,                // slave capital city flag?
        AREA_FLAG_ALLOW_DUELS           = 0x00000040,                // allow to duel here
        AREA_FLAG_ARENA                 = 0x00000080,                // arena, both instanced and world arenas
        AREA_FLAG_CAPITAL               = 0x00000100,                // main capital city flag
        AREA_FLAG_CITY                  = 0x00000200,                // only for one zone named "City" (where it located?)
        AREA_FLAG_OUTLAND               = 0x00000400,                // expansion zones? (only Eye of the Storm not have this flag, but have 0x00004000 flag)
        AREA_FLAG_SANCTUARY             = 0x00000800,                // sanctuary area (PvP disabled)
        AREA_FLAG_NEED_FLY              = 0x00001000,                // Respawn alive at the graveyard without corpse
        AREA_FLAG_UNUSED1               = 0x00002000,                // Unused in 3.3.5a
        AREA_FLAG_OUTLAND2              = 0x00004000,                // expansion zones? (only Circle of Blood Arena not have this flag, but have 0x00000400 flag)
        AREA_FLAG_OUTDOOR_PVP           = 0x00008000,                // pvp objective area? (Death's Door also has this flag although it's no pvp object area)
        AREA_FLAG_ARENA_INSTANCE        = 0x00010000,                // used by instanced arenas only
        AREA_FLAG_UNUSED2               = 0x00020000,                // Unused in 3.3.5a
        AREA_FLAG_CONTESTED_AREA        = 0x00040000,                // On PvP servers these areas are considered contested, even though the zone it is contained in is a Horde/Alliance territory.
        AREA_FLAG_UNK6                  = 0x00080000,                // Valgarde and Acherus: The Ebon Hold
        AREA_FLAG_LOWLEVEL              = 0x00100000,                // used for some starting areas with area_level <= 15
        AREA_FLAG_TOWN                  = 0x00200000,                // small towns with Inn
        AREA_FLAG_REST_ZONE_HORDE       = 0x00400000,                // Warsong Hold, Acherus: The Ebon Hold, New Agamand Inn, Vengeance Landing Inn, Sunreaver Pavilion (Something to do with team?)
        AREA_FLAG_REST_ZONE_ALLIANCE    = 0x00800000,                // Valgarde, Acherus: The Ebon Hold, Westguard Inn, Silver Covenant Pavilion (Something to do with team?)
        AREA_FLAG_WINTERGRASP           = 0x01000000,                // Wintergrasp and it's subzones
        AREA_FLAG_INSIDE                = 0x02000000,                // used for determinating spell related inside/outside questions in Map::IsOutdoors
        AREA_FLAG_OUTSIDE               = 0x04000000,                // used for determinating spell related inside/outside questions in Map::IsOutdoors
        AREA_FLAG_WINTERGRASP_2         = 0x08000000,                // Can Hearth And Resurrect From Area
        AREA_FLAG_NO_FLY_ZONE           = 0x20000000,                // Marks zones where you cannot fly
        AREA_FLAG_UNK9                  = 0x40000000
    };
    
    enum Difficulty : uint8
    {
        DIFFICULTY_NONE           = 0,
        DIFFICULTY_NORMAL         = 1,
        DIFFICULTY_HEROIC         = 2,
        DIFFICULTY_10_N           = 3,
        DIFFICULTY_25_N           = 4,
        DIFFICULTY_10_HC          = 5,
        DIFFICULTY_25_HC          = 6,
        DIFFICULTY_LFR            = 7,
        DIFFICULTY_CHALLENGE      = 8,
        DIFFICULTY_40             = 9,
        DIFFICULTY_HC_SCENARIO    = 11,
        DIFFICULTY_N_SCENARIO     = 12,
        DIFFICULTY_NORMAL_RAID    = 14,
        DIFFICULTY_HEROIC_RAID    = 15,
        DIFFICULTY_MYTHIC_RAID    = 16,
        DIFFICULTY_LFR_NEW        = 17,
        DIFFICULTY_EVENT_RAID     = 18,
        DIFFICULTY_EVENT_DUNGEON  = 19,
        DIFFICULTY_EVENT_SCENARIO = 20,
    
        MAX_DIFFICULTY
    };
    
    enum DifficultyFlags
    {
        DIFFICULTY_FLAG_HEROIC          = 0x01,
        DIFFICULTY_FLAG_DEFAULT         = 0x02,
        DIFFICULTY_FLAG_CAN_SELECT      = 0x04, // Player can select this difficulty in dropdown menu
        DIFFICULTY_FLAG_CHALLENGE_MODE  = 0x08,
    
        DIFFICULTY_FLAG_LEGACY          = 0x20,
        DIFFICULTY_FLAG_DISPLAY_HEROIC  = 0x40, // Controls icon displayed on minimap when inside the instance
        DIFFICULTY_FLAG_DISPLAY_MYTHIC  = 0x80  // Controls icon displayed on minimap when inside the instance
    };
    
    enum SpawnMask
    {
        SPAWNMASK_CONTINENT = (1 << DIFFICULTY_NONE), // any maps without spawn modes
    
        SPAWNMASK_DUNGEON_NORMAL    = (1 << DIFFICULTY_NORMAL),
        SPAWNMASK_DUNGEON_HEROIC    = (1 << DIFFICULTY_HEROIC),
        SPAWNMASK_DUNGEON_ALL       = (SPAWNMASK_DUNGEON_NORMAL | SPAWNMASK_DUNGEON_HEROIC),
    
        SPAWNMASK_RAID_10MAN_NORMAL = (1 << DIFFICULTY_10_N),
        SPAWNMASK_RAID_25MAN_NORMAL = (1 << DIFFICULTY_25_N),
        SPAWNMASK_RAID_NORMAL_ALL   = (SPAWNMASK_RAID_10MAN_NORMAL | SPAWNMASK_RAID_25MAN_NORMAL),
    
        SPAWNMASK_RAID_10MAN_HEROIC = (1 << DIFFICULTY_10_HC),
        SPAWNMASK_RAID_25MAN_HEROIC = (1 << DIFFICULTY_25_HC),
        SPAWNMASK_RAID_HEROIC_ALL   = (SPAWNMASK_RAID_10MAN_HEROIC | SPAWNMASK_RAID_25MAN_HEROIC),
    
        SPAWNMASK_RAID_ALL          = (SPAWNMASK_RAID_NORMAL_ALL | SPAWNMASK_RAID_HEROIC_ALL)
    };
    
    enum FactionTemplateFlags
    {
        FACTION_TEMPLATE_FLAG_PVP               = 0x00000800,   // flagged for PvP
        FACTION_TEMPLATE_FLAG_CONTESTED_GUARD   = 0x00001000,   // faction will attack players that were involved in PvP combats
        FACTION_TEMPLATE_FLAG_HOSTILE_BY_DEFAULT= 0x00002000
    };
    
    enum FactionMasks
    {
        FACTION_MASK_PLAYER   = 1,                              // any player
        FACTION_MASK_ALLIANCE = 2,                              // player or creature from alliance team
        FACTION_MASK_HORDE    = 4,                              // player or creature from horde team
        FACTION_MASK_MONSTER  = 8                               // aggressive creature from monster team
        // if none flags set then non-aggressive creature
    };
    
    enum MapTypes                                               // Lua_IsInInstance
    {
        MAP_COMMON          = 0,                                // none
        MAP_INSTANCE        = 1,                                // party
        MAP_RAID            = 2,                                // raid
        MAP_BATTLEGROUND    = 3,                                // pvp
        MAP_ARENA           = 4,                                // arena
        MAP_SCENARIO        = 5                                 // scenario
    };
    
    enum MapFlags
    {
        MAP_FLAG_CAN_TOGGLE_DIFFICULTY  = 0x0100,
        MAP_FLAG_FLEX_LOCKING           = 0x8000, // All difficulties share completed encounters lock, not bound to a single instance id
                                                  // heroic difficulty flag overrides it and uses instance id bind
    };
    
    enum AbilytyLearnType
    {
        SKILL_LINE_ABILITY_LEARNED_ON_SKILL_VALUE  = 1, // Spell state will update depending on skill value
        SKILL_LINE_ABILITY_LEARNED_ON_SKILL_LEARN  = 2  // Spell will be learned/removed together with entire skill
    };
    
    enum ItemEnchantmentType
    {
        ITEM_ENCHANTMENT_TYPE_NONE             = 0,
        ITEM_ENCHANTMENT_TYPE_COMBAT_SPELL     = 1,
        ITEM_ENCHANTMENT_TYPE_DAMAGE           = 2,
        ITEM_ENCHANTMENT_TYPE_EQUIP_SPELL      = 3,
        ITEM_ENCHANTMENT_TYPE_RESISTANCE       = 4,
        ITEM_ENCHANTMENT_TYPE_STAT             = 5,
        ITEM_ENCHANTMENT_TYPE_TOTEM            = 6,
        ITEM_ENCHANTMENT_TYPE_USE_SPELL        = 7,
        ITEM_ENCHANTMENT_TYPE_PRISMATIC_SOCKET = 8
    };
    
    enum ItemExtendedCostFlags
    {
        ITEM_EXT_COST_FLAG_REQUIRE_GUILD                = 0x01,
        ITEM_EXT_COST_CURRENCY_REQ_IS_SEASON_EARNED_1   = 0x02,
        ITEM_EXT_COST_CURRENCY_REQ_IS_SEASON_EARNED_2   = 0x04,
        ITEM_EXT_COST_CURRENCY_REQ_IS_SEASON_EARNED_3   = 0x08,
        ITEM_EXT_COST_CURRENCY_REQ_IS_SEASON_EARNED_4   = 0x10,
        ITEM_EXT_COST_CURRENCY_REQ_IS_SEASON_EARNED_5   = 0x20,
    };
    
    enum ItemBonusType
    {
        ITEM_BONUS_ITEM_LEVEL     = 1,
        ITEM_BONUS_STAT           = 2,
        ITEM_BONUS_QUALITY        = 3,
        ITEM_BONUS_DESCRIPTION    = 4,
        ITEM_BONUS_SUFFIX         = 5,
        ITEM_BONUS_SOCKET         = 6,
        ITEM_BONUS_APPEARANCE     = 7,
        ITEM_BONUS_REQUIRED_LEVEL = 8,
    };
    
    enum ItemLimitCategoryMode
    {
        ITEM_LIMIT_CATEGORY_MODE_HAVE       = 0,                      // limit applied to amount items in inventory/bank
        ITEM_LIMIT_CATEGORY_MODE_EQUIP      = 1                       // limit applied to amount equipped items (including used gems)
    };
    
    enum MountCapabilityFlags
    {
        MOUNT_CAPABILITY_FLAG_CAN_PITCH     = 0x4,                    // client checks MOVEMENTFLAG2_FULL_SPEED_PITCHING
        MOUNT_CAPABILITY_FLAG_CAN_SWIM      = 0x8,                    // client checks MOVEMENTFLAG_SWIMMING
    };
    
    enum MountFlags
    {
        MOUNT_FLAG_SELF_MOUNT               = 0x02,                   // Player becomes the mount himself
        MOUNT_FLAG_FACTION_SPECIFIC         = 0x04,
        MOUNT_FLAG_PREFERRED_SWIMMING       = 0x10,
        MOUNT_FLAG_PREFERRED_WATER_WALKING  = 0x20,
        MOUNT_FLAG_HIDE_IF_UNKNOWN          = 0x40
    };
    
    enum SkillRaceClassInfoFlags
    {
        SKILL_FLAG_NO_SKILLUP_MESSAGE       = 0x2,
        SKILL_FLAG_ALWAYS_MAX_VALUE         = 0x10,
        SKILL_FLAG_UNLEARNABLE              = 0x20,     // Skill can be unlearned
        SKILL_FLAG_INCLUDE_IN_SORT          = 0x80,     // Spells belonging to a skill with this flag will additionally compare skill ids when sorting spellbook in client
        SKILL_FLAG_NOT_TRAINABLE            = 0x100,
        SKILL_FLAG_MONO_VALUE               = 0x400     // Skill always has value 1 - clientside display flag, real value can be different
    };
    
    enum SpellCategoryFlags
    {
        SPELL_CATEGORY_FLAG_COOLDOWN_SCALES_WITH_WEAPON_SPEED   = 0x01, // unused
        SPELL_CATEGORY_FLAG_COOLDOWN_STARTS_ON_EVENT            = 0x04,
        SPELL_CATEGORY_FLAG_COOLDOWN_EXPIRES_AT_DAILY_RESET     = 0x08
    };
    
    enum TotemCategoryType
    {
        TOTEM_CATEGORY_TYPE_KNIFE           = 1,
        TOTEM_CATEGORY_TYPE_TOTEM           = 2,
        TOTEM_CATEGORY_TYPE_ROD             = 3,
        TOTEM_CATEGORY_TYPE_PICK            = 21,
        TOTEM_CATEGORY_TYPE_STONE           = 22,
        TOTEM_CATEGORY_TYPE_HAMMER          = 23,
        TOTEM_CATEGORY_TYPE_SPANNER         = 24
    };
    
    // SummonProperties.dbc, col 1
    enum SummonPropGroup
    {
        SUMMON_PROP_GROUP_UNKNOWN1       = 0,                   // 1160 spells in 3.0.3
        SUMMON_PROP_GROUP_UNKNOWN2       = 1,                   // 861 spells in 3.0.3
        SUMMON_PROP_GROUP_PETS           = 2,                   // 52 spells in 3.0.3, pets mostly
        SUMMON_PROP_GROUP_CONTROLLABLE   = 3,                   // 13 spells in 3.0.3, mostly controllable
        SUMMON_PROP_GROUP_UNKNOWN3       = 4                    // 86 spells in 3.0.3, taxi/mounts
    };
    
    // SummonProperties.dbc, col 3
    enum SummonPropType
    {
        SUMMON_PROP_TYPE_UNKNOWN         = 0,                   // different summons, 1330 spells in 3.0.3
        SUMMON_PROP_TYPE_SUMMON          = 1,                   // generic summons, 49 spells in 3.0.3
        SUMMON_PROP_TYPE_GUARDIAN        = 2,                   // summon guardian, 393 spells in 3.0.3
        SUMMON_PROP_TYPE_ARMY            = 3,                   // summon army, 5 spells in 3.0.3
        SUMMON_PROP_TYPE_TOTEM           = 4,                   // summon totem, 169 spells in 3.0.3
        SUMMON_PROP_TYPE_CRITTER         = 5,                   // critter/minipet, 195 spells in 3.0.3
        SUMMON_PROP_TYPE_DK              = 6,                   // summon DRW/Ghoul, 2 spells in 3.0.3
        SUMMON_PROP_TYPE_BOMB            = 7,                   // summon bot/bomb, 4 spells in 3.0.3
        SUMMON_PROP_TYPE_PHASING         = 8,                   // something todo with DK prequest line, 2 spells in 3.0.3
        SUMMON_PROP_TYPE_SIEGE_VEH       = 9,                   // summon different vehicles, 14 spells in 3.0.3
        SUMMON_PROP_TYPE_DRAKE_VEH       = 10,                  // summon drake (vehicle), 3 spells
        SUMMON_PROP_TYPE_LIGHTWELL       = 11,                  // summon lightwell, 6 spells in 3.0.3
        SUMMON_PROP_TYPE_JEEVES          = 12,                  // summon Jeeves, 1 spell in 3.3.5a
        SUMMON_PROP_TYPE_LASHTAIL        = 13                   // Lashtail Hatchling, 1 spell in 4.2.2
    };
    
    // SummonProperties.dbc, col 5
    enum SummonPropFlags
    {
        SUMMON_PROP_FLAG_NONE            = 0x00000000,          // 1342 spells in 3.0.3
        SUMMON_PROP_FLAG_UNK1            = 0x00000001,          // 75 spells in 3.0.3, something unfriendly
        SUMMON_PROP_FLAG_UNK2            = 0x00000002,          // 616 spells in 3.0.3, something friendly
        SUMMON_PROP_FLAG_UNK3            = 0x00000004,          // 22 spells in 3.0.3, no idea...
        SUMMON_PROP_FLAG_UNK4            = 0x00000008,          // 49 spells in 3.0.3, some mounts
        SUMMON_PROP_FLAG_UNK5            = 0x00000010,          // 25 spells in 3.0.3, quest related?
        SUMMON_PROP_FLAG_UNK6            = 0x00000020,          // 0 spells in 3.3.5, unused
        SUMMON_PROP_FLAG_UNK7            = 0x00000040,          // 12 spells in 3.0.3, no idea
        SUMMON_PROP_FLAG_UNK8            = 0x00000080,          // 4 spells in 3.0.3, no idea
        SUMMON_PROP_FLAG_UNK9            = 0x00000100,          // 51 spells in 3.0.3, no idea, many quest related
        SUMMON_PROP_FLAG_UNK10           = 0x00000200,          // 51 spells in 3.0.3, something defensive
        SUMMON_PROP_FLAG_UNK11           = 0x00000400,          // 3 spells, requires something near?
        SUMMON_PROP_FLAG_UNK12           = 0x00000800,          // 30 spells in 3.0.3, no idea
        SUMMON_PROP_FLAG_UNK13           = 0x00001000,          // Lightwell, Jeeves, Gnomish Alarm-o-bot, Build vehicles(wintergrasp)
        SUMMON_PROP_FLAG_UNK14           = 0x00002000,          // Guides, player follows
        SUMMON_PROP_FLAG_UNK15           = 0x00004000,          // Force of Nature, Shadowfiend, Feral Spirit, Summon Water Elemental
        SUMMON_PROP_FLAG_UNK16           = 0x00008000,          // Light/Dark Bullet, Soul/Fiery Consumption, Twisted Visage, Twilight Whelp. Phase related?
        SUMMON_PROP_FLAG_UNK17           = 0x00010000,
        SUMMON_PROP_FLAG_UNK18           = 0x00020000,
        SUMMON_PROP_FLAG_UNK19           = 0x00040000,
        SUMMON_PROP_FLAG_UNK20           = 0x00080000,
        SUMMON_PROP_FLAG_UNK21           = 0x00100000           // Totems
    };
    
    enum VehicleSeatFlags
    {
        VEHICLE_SEAT_FLAG_HAS_LOWER_ANIM_FOR_ENTER                         = 0x00000001,
        VEHICLE_SEAT_FLAG_HAS_LOWER_ANIM_FOR_RIDE                          = 0x00000002,
        VEHICLE_SEAT_FLAG_UNK3                                             = 0x00000004,
        VEHICLE_SEAT_FLAG_SHOULD_USE_VEH_SEAT_EXIT_ANIM_ON_VOLUNTARY_EXIT  = 0x00000008,
        VEHICLE_SEAT_FLAG_UNK5                                             = 0x00000010,
        VEHICLE_SEAT_FLAG_UNK6                                             = 0x00000020,
        VEHICLE_SEAT_FLAG_UNK7                                             = 0x00000040,
        VEHICLE_SEAT_FLAG_UNK8                                             = 0x00000080,
        VEHICLE_SEAT_FLAG_UNK9                                             = 0x00000100,
        VEHICLE_SEAT_FLAG_HIDE_PASSENGER                                   = 0x00000200, // Passenger is hidden
        VEHICLE_SEAT_FLAG_ALLOW_TURNING                                    = 0x00000400, // needed for CGCamera__SyncFreeLookFacing
        VEHICLE_SEAT_FLAG_CAN_CONTROL                                      = 0x00000800, // Lua_UnitInVehicleControlSeat
        VEHICLE_SEAT_FLAG_CAN_CAST_MOUNT_SPELL                             = 0x00001000, // Can cast spells with SPELL_AURA_MOUNTED from seat (possibly 4.x only, 0 seats on 3.3.5a)
        VEHICLE_SEAT_FLAG_UNCONTROLLED                                     = 0x00002000, // can override !& VEHICLE_SEAT_FLAG_CAN_ENTER_OR_EXIT
        VEHICLE_SEAT_FLAG_CAN_ATTACK                                       = 0x00004000, // Can attack, cast spells and use items from vehicle
        VEHICLE_SEAT_FLAG_SHOULD_USE_VEH_SEAT_EXIT_ANIM_ON_FORCED_EXIT     = 0x00008000,
        VEHICLE_SEAT_FLAG_UNK17                                            = 0x00010000,
        VEHICLE_SEAT_FLAG_UNK18                                            = 0x00020000, // Needs research and support (28 vehicles): Allow entering vehicles while keeping specific permanent(?) auras that impose visuals (states like beeing under freeze/stun mechanic, emote state animations).
        VEHICLE_SEAT_FLAG_HAS_VEH_EXIT_ANIM_VOLUNTARY_EXIT                 = 0x00040000,
        VEHICLE_SEAT_FLAG_HAS_VEH_EXIT_ANIM_FORCED_EXIT                    = 0x00080000,
        VEHICLE_SEAT_FLAG_PASSENGER_NOT_SELECTABLE                         = 0x00100000,
        VEHICLE_SEAT_FLAG_UNK22                                            = 0x00200000,
        VEHICLE_SEAT_FLAG_REC_HAS_VEHICLE_ENTER_ANIM                       = 0x00400000,
        VEHICLE_SEAT_FLAG_IS_USING_VEHICLE_CONTROLS                        = 0x00800000, // Lua_IsUsingVehicleControls
        VEHICLE_SEAT_FLAG_ENABLE_VEHICLE_ZOOM                              = 0x01000000,
        VEHICLE_SEAT_FLAG_CAN_ENTER_OR_EXIT                                = 0x02000000, // Lua_CanExitVehicle - can enter and exit at free will
        VEHICLE_SEAT_FLAG_CAN_SWITCH                                       = 0x04000000, // Lua_CanSwitchVehicleSeats
        VEHICLE_SEAT_FLAG_HAS_START_WARITING_FOR_VEH_TRANSITION_ANIM_ENTER = 0x08000000,
        VEHICLE_SEAT_FLAG_HAS_START_WARITING_FOR_VEH_TRANSITION_ANIM_EXIT  = 0x10000000,
        VEHICLE_SEAT_FLAG_CAN_CAST                                         = 0x20000000, // Lua_UnitHasVehicleUI
        VEHICLE_SEAT_FLAG_UNK2                                             = 0x40000000, // checked in conjunction with 0x800 in CastSpell2
        VEHICLE_SEAT_FLAG_ALLOWS_INTERACTION                               = 0x80000000
    };
    
    enum VehicleSeatFlagsB
    {
        VEHICLE_SEAT_FLAG_B_NONE                     = 0x00000000,
        VEHICLE_SEAT_FLAG_B_USABLE_FORCED            = 0x00000002,
        VEHICLE_SEAT_FLAG_B_TARGETS_IN_RAIDUI        = 0x00000008,           // Lua_UnitTargetsVehicleInRaidUI
        VEHICLE_SEAT_FLAG_B_EJECTABLE                = 0x00000020,           // ejectable
        VEHICLE_SEAT_FLAG_B_USABLE_FORCED_2          = 0x00000040,
        VEHICLE_SEAT_FLAG_B_USABLE_FORCED_3          = 0x00000100,
        VEHICLE_SEAT_FLAG_B_KEEP_PET                 = 0x00020000,
        VEHICLE_SEAT_FLAG_B_USABLE_FORCED_4          = 0x02000000,
        VEHICLE_SEAT_FLAG_B_CAN_SWITCH               = 0x04000000,
        VEHICLE_SEAT_FLAG_B_VEHICLE_PLAYERFRAME_UI   = 0x80000000            // Lua_UnitHasVehiclePlayerFrameUI - actually checked for flagsb &~ 0x80000000
    };
    
    // CurrencyTypes.dbc
    enum CurrencyTypes
    {
        CURRENCY_TYPE_CONQUEST_POINTS       = 390,
        CURRENCY_TYPE_HONOR_POINTS          = 392,
        CURRENCY_TYPE_JUSTICE_POINTS        = 395,
        CURRENCY_TYPE_VALOR_POINTS          = 396,
        CURRENCY_TYPE_CONQUEST_META_ARENA   = 483,
        CURRENCY_TYPE_CONQUEST_META_RBG     = 484,
        CURRENCY_TYPE_APEXIS_CRYSTALS       = 823,
    };