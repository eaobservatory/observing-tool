#!/local/perl/bin/perl

$tag = shift or die "No arguments from you\n" ;

$base = 'file:///jac_sw/gitroot/ot.git' ;

@git_args = ( 'git' , 'clone' , $base , 'ompot' ) ;
if( system @git_args ){ die "Could not clone.\n" ; }

chdir ompot ;

@git_args = ( 'git' , 'tag' , $tag ) ;
if( system @git_args ){ die "Could not tag release $tag.\n" ; }

@git_args = ( 'git' , 'push' , 'origin' ) ;
if( system @git_args ){ die "Could not push tagged release.\n" ; }