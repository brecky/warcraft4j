# Description
NGDP ( _Next Generation Data Protocol_ ) is Blizzard's implementation for providing information for online CASC CDN repositories (mainly for use by installers and the Battle.NET desktop client).
Blizzard provides a number of online files providing the information required to access a CASC CDN for the desired region and game.

# NGDP game codes
The NGDP methods are game specific, just as the CDN data, and take a game code argument indicating which game the method request is for.

 * _wow_ - World of Warcraft
 * _wowt_ - World of Warcraft PTR (Public Test Realm)
 * _wow_beta_ - World of Warcraft Beta
 * _heroes_ - Heroes of the Storm

# CDNS
The CDN host information for a game for all regions.

## Method URL
    http://us.patch.battle.net:1119/(GameCode)/cdns

## Result data format
    Name!STRING:0|Path!STRING:0|Hosts!STRING:0

 * _Name_ - The region key (e.g. eu or us)
 * _Path_ - The root CDN path on the host.
 * _Hosts_ - Space separated list of the CDN hosts for the region.
 
# Build Config
The base CASC information for the last game version. 

## Method URL
    http://us.patch.battle.net:1119/(GameCode)/versions

## Result data format
    Region!STRING:0|BuildConfig!HEX:16|CDNConfig!HEX:16|BuildId!DEC:4|VersionsName!String:0

 * _Region_ - The region key (e.g. eu or us)
 * _BuildConfig_ - The hash of the _.build.config_ file.
 * _CDNConfig_ - The hash of the CDN configuration file. 
 * _BuildId_ - The (last) build number of the game.
 * _VersionsName_ - The (last) full version String of the game.